package com.aginsurance.verzekeringsweg.views;

import com.aginsurance.verzekeringsweg.Util.Checker;
import com.aginsurance.verzekeringsweg.persistance.DTOs.VraagDTO;
import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import com.aginsurance.verzekeringsweg.persistance.services.HoofdDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.SubDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.VraagService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.stream.Stream;

@Route("vragen")
@PageTitle("Vragen overzicht")
@RouteAlias("questions")
public class VragenView extends VerticalLayout implements AfterNavigationObserver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VraagService vraagService;
    private final HoofdDomeinService hoofdDomeinService;
    private final SubDomeinService subDomeinService;
    private HorizontalLayout filtering;
    private ComboBox<HoofdDomein> hoofdDomeinBox;
    private Button clearHoofd;
    private ComboBox<SubDomein> subDomeinBox;
    private Button clearSub;
    private Grid<Vraag> vragen;
    private TextArea text;
    private Anchor download;
    private Button downloadBtn;
    private Upload upload;
    private FileBuffer buffer;
    private Button delete;


    @Autowired
    public VragenView(VraagService vraagService, HoofdDomeinService hoofdDomeinService, SubDomeinService subDomeinService) {
        this.vraagService = vraagService;
        this.hoofdDomeinService = hoofdDomeinService;
        this.subDomeinService = subDomeinService;

        this.filtering = new HorizontalLayout();
        filtering.setWidthFull();

        this.hoofdDomeinBox = new ComboBox<>("Hoofd domein");
        this.subDomeinBox = new ComboBox<>("Sub domein");

        this.download = new Anchor(new StreamResource("filename.xlsx", () -> new ByteArrayInputStream(createResource())), "");
        this.downloadBtn = new Button(new Icon(VaadinIcon.DOWNLOAD));
        download.getElement().setAttribute("download", true);
        download.add(downloadBtn);
        download.getStyle().set("margin-left"," auto");

        buffer = new FileBuffer();
        upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(buffer.getFileData().getFile());
                upload(fileInputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        vragen = new Grid<>(Vraag.class,false);
        vragen.addColumn(Vraag::getVraag).setHeader("Vraag");
        vragen.addColumn(Vraag::getJuistAntwoord).setHeader("Juist antwoord");
        vragen.addColumn(Vraag::getFout1).setHeader("Foute antwoorden");
        vragen.addColumn(Vraag::getFout2).setHeader("");


        text = new TextArea("Details");
        text.setReadOnly(true);
        text.setWidthFull();

        this.clearHoofd = new Button(new Icon(VaadinIcon.CLOSE));
        clearHoofd.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        clearHoofd.addClickListener(e -> hoofdDomeinBox.setValue(null));

        this.clearSub = new Button(new Icon(VaadinIcon.CLOSE));
        clearSub.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        clearSub.addClickListener(e -> subDomeinBox.setValue(null));

        filtering.add(hoofdDomeinBox,clearHoofd,subDomeinBox,clearSub, download, upload);
        filtering.setAlignItems(Alignment.BASELINE);

        this.add(filtering, vragen, text);

    }

    private byte[] createResource() {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Vragen");
        //sheet.setColumnWidth(0, 6000);
        //sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        //headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        //headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        //font.setFontName("Arial");
        //font.setFontHeightInPoints((short) 16);
        //font.setBold(true);
        //headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Vraag");
        //headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Juist antwoord");
        //headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Foute antwoorden");
        sheet.addMergedRegion(new CellRangeAddress(0,0,2,3));

        headerCell = header.createCell(4);
        headerCell.setCellValue("Hoofddomein(en)");

        headerCell = header.createCell(5);
        headerCell.setCellValue("Subdomein(en)");


        final int[] y = {1};
        vraagService.alleVragenExcel().forEach(vraag -> {
            Row row = sheet.createRow(y[0]);

            Cell werkCell = row.createCell(0);
            werkCell.setCellValue(vraag.getVraag());

            werkCell = row.createCell(1);
            werkCell.setCellValue(vraag.getJuistAntwoord());

            werkCell = row.createCell(2);
            werkCell.setCellValue(vraag.getFout1());

            werkCell = row.createCell(3);
            werkCell.setCellValue(vraag.getFout2());

            //TODO fix me lazy dingdongs
            werkCell = row.createCell(4);
            werkCell.setCellValue(vraag.getHoofdDomeinen());

            werkCell = row.createCell(5);
            werkCell.setCellValue(vraag.getSubDomeinen());
           y[0]++;
        });


        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);



        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

        text.setValue(vraagService.alleVragen().get(0).toString());
        vragen.setItems(vraagService.alleVragen());
        hoofdDomeinBox.setItems(hoofdDomeinService.alles());
        hoofdDomeinBox.setItemLabelGenerator(HoofdDomein::getNaam);
        subDomeinBox.setItemLabelGenerator(SubDomein::getNaam);
        hoofdDomeinBox.addValueChangeListener(valueChangeEvent ->{
            if (valueChangeEvent.getValue() == null) {
                vragen.setItems(vraagService.alleVragen());
                subDomeinBox.setItems(Stream.empty());
            }
            else {
                subDomeinBox.setItems(valueChangeEvent.getValue().getSubdomeinen());
                //TODO vragen of gewoon losse vragen oke is of niet, als wel vraag fix aan Nadir
                vragen.setItems(valueChangeEvent.getValue().alleVragenInDomein());
                //OM ENKEL LOSSE VRAGEN TE KRIJGEN ( ni vergeten veranderen in if van sub)
                //vragen.setItems(valueChangeEvent.getValue().getVragen());
            }
        });
        subDomeinBox.addValueChangeListener(e ->{
            if (e.getValue() == null){

                if (hoofdDomeinBox.getValue() == null) {
                    vragen.setItems(vraagService.alleVragen());

                }
                else vragen.setItems(hoofdDomeinBox.getValue().alleVragenInDomein());//vragen.setItems(hoofdDomeinBox.getValue().getVragen());
            }
            else vragen.setItems(e.getValue().getVragen());
                });
        vragen.addItemClickListener(vraagItemClickEvent -> text.setValue(vraagItemClickEvent.getItem().toString()));

    }

    public void upload(FileInputStream file){
        hoofdDomeinService.verwijderAlles();

        subDomeinService.verwijderAlles();
        vraagService.verwijderAlles();

        try {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for (Row row: sheet){
                if (row.getRowNum() != 0) {
                    VraagDTO vraag = new VraagDTO();
                    for (Cell cell: row){
                        String inhoud = "";
                        switch (cell.getCellType()){
                            case STRING:
                                inhoud = cell.getRichStringCellValue().getString();
                                break;
                            case NUMERIC:
                                inhoud = (DateUtil.isCellDateFormatted(cell)?cell.getDateCellValue() + "":cell.getNumericCellValue() +"");
                                break;
                            case BOOLEAN:
                                inhoud = cell.getBooleanCellValue() + "";
                                break;
                            case FORMULA:
                                inhoud = cell.getCellFormula();
                                break;
                        }
                        if (Checker.String(inhoud)){
                            switch (cell.getColumnIndex()){
                                case 0:
                                    vraag.setVraag(inhoud);
                                    break;
                                case 1:
                                    vraag.setJuistAntwoord(inhoud);
                                    break;
                                case 2:
                                    vraag.setFout1(inhoud);
                                    break;
                                case 3:
                                    vraag.setFout2(inhoud);
                                    break;
                                case 4:
                                    vraag.setHoofdDomeinen(inhoud);
                                    break;
                                case 5:
                                    vraag.setSubDomeinen(inhoud);
                                    break;

                            }

                        }
                    }
                    vraag.naarVraag(hoofdDomeinService,subDomeinService,vraagService);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        UI.getCurrent().getPage().reload();
    }

}
