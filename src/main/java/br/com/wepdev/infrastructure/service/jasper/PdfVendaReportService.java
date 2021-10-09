package br.com.wepdev.infrastructure.service.jasper;

import br.com.wepdev.domain.filter.VendaDiariaFilter;
import br.com.wepdev.domain.service.VendaQueryService;
import br.com.wepdev.domain.service.VendaReportService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

/**
 * Precisa ser adicionado a api do Jasper Reports,
 */
@Service
public class PdfVendaReportService implements VendaReportService {


    @Autowired
    private VendaQueryService vendaQueryService;


    /**
     * Metodo que gera PDF
     *
     * @param filtro
     * @param timeOffset
     * @return
     */
    @Override
    public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {

        try {

        /*
        Pegando o fluxo de dados de um arquivo de dentro do projeto, pegando o arquivo jasper que foi feito e compilado no jaspersoft,
        e ja retorna como Stream
         */
            var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

            /**
             * A chave e String e o valor e um Object
             */
            var parametros = new HashMap<String, Object>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

            var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset); // Chamando o metodo para obter a coleção de vendas diarias
            var dataSource = new JRBeanCollectionDataSource(vendasDiarias); // Colocando a colecao de vendas diarias dentro do dataSource

            var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        }catch (Exception e){
              throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
        }
    }

















}
