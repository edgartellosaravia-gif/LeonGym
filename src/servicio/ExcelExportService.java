package servicio;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositorio.ReporteRepository.ReporteFila;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Exporta a un archivo Excel (.xlsx) los reportes de incidencia, usando
 * Apache POI. Es solo lectura sobre los datos: no modifica la base de datos.
 */
public final class ExcelExportService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelExportService.class);
    private static final String[] ENCABEZADOS = {"ID", "Empleado", "Mensaje", "Fecha", "Estado"};

    private ExcelExportService() {
    }

    public static void exportarReportes(List<ReporteFila> filas, File destino) throws IOException {
        try (Workbook libro = new XSSFWorkbook()) {
            Sheet hoja = libro.createSheet("Reportes");

            Font negrita = libro.createFont();
            negrita.setBold(true);
            CellStyle estiloEncabezado = libro.createCellStyle();
            estiloEncabezado.setFont(negrita);

            Row cabecera = hoja.createRow(0);
            for (int i = 0; i < ENCABEZADOS.length; i++) {
                Cell celda = cabecera.createCell(i);
                celda.setCellValue(ENCABEZADOS[i]);
                celda.setCellStyle(estiloEncabezado);
            }

            int numFila = 1;
            for (ReporteFila f : filas) {
                Row fila = hoja.createRow(numFila++);
                fila.createCell(0).setCellValue(f.idReporte());
                fila.createCell(1).setCellValue(f.empleado());
                fila.createCell(2).setCellValue(f.mensaje());
                fila.createCell(3).setCellValue(f.fecha());
                fila.createCell(4).setCellValue(f.estado());
            }
            for (int i = 0; i < ENCABEZADOS.length; i++) {
                hoja.autoSizeColumn(i);
            }

            try (FileOutputStream salida = new FileOutputStream(destino)) {
                libro.write(salida);
            }
            LOG.info("Excel de reportes generado: {} ({} filas)", destino.getAbsolutePath(), filas.size());
        }
    }
}
