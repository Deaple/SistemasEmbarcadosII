package br.edu.ifba.se.cozinhaindustrial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

import br.edu.ifba.se.cozinhaindustrial.conector.SingleConector;

@SuppressWarnings("serial")
@ManagedBean(name = "monitor")
public class Monitor implements Serializable {
	private MeterGaugeChartModel modeloMedidorGas;
	private MeterGaugeChartModel modeloMedidorChamas;
	private PieChartModel modeloGraficoPizza;
	private LineChartModel modeloGraficoLinha;
	private static int segundos=0;

	@PostConstruct
	public void iniciar() {
		configurarMedidor();
		configurarGraficoPizza();
		configurarGraficoLinha();
	}
	
	public Informacao lerSensoresRetorno() {
		Informacao info = SingleConector.getInformacao();
		return info;
	}

	public void lerSensores() {
		Informacao info = SingleConector.getInformacao();
		int temperatura = info.getTemperatura();
		int gas = info.getGas();
		int chamas = info.getChamas();
		System.out.println("Temperatura: " + temperatura + " Gas: " + gas + " Chamas: " + chamas);
		modeloMedidorGas.setValue(gas);
		modeloMedidorChamas.setValue(chamas);
	}

	private void configurarMedidor() {
		modeloMedidorGas = criarModeloMedidorGas();
		modeloMedidorGas.setTitle("Vazamento de Gás");
		modeloMedidorGas.setGaugeLabel("%");
		modeloMedidorGas.setSeriesColors("7CFC00,FFFF00,FFA500,f97b4e,FF0000");
		
		modeloMedidorChamas = criarModeloMedidorChamas();
		modeloMedidorChamas.setTitle("Detecção de Incêndio");
		modeloMedidorChamas.setGaugeLabel("%");
		modeloMedidorChamas.setSeriesColors("cdede7,50d380,ead539,fa7929,f42f2f");
	}
	
	private void configurarGraficoLinha(){
			modeloGraficoLinha = iniciarModeloLinha();
			modeloGraficoLinha.setTitle("Medidor Temperatura(°C)");
			modeloGraficoLinha.setLegendPosition("w");
			Axis yAxis = modeloGraficoLinha.getAxis(AxisType.Y);
			yAxis.setMin(0);
			yAxis.setMax(100);
			
			Axis xAxis = modeloGraficoLinha.getAxis(AxisType.X);
			xAxis.setMin(0);
	}
	
	private LineChartModel iniciarModeloLinha(){
		LineChartModel modelo = new LineChartModel();
		LineChartSeries seriesTemperatura = new LineChartSeries("Sensor Temperatura");
		Informacao info = SingleConector.getInformacao();
		seriesTemperatura.setFill(true);
		seriesTemperatura.set(0,0);
		//variação do tempo em segundos
		segundos = segundoAtual();
		seriesTemperatura.set(segundos, info.getTemperatura());
	
		modelo.addSeries(seriesTemperatura);
		return modelo;
	}
	
	public LineChartModel getModeloGraficoLinha(){
		return modeloGraficoLinha;
	}
	private MeterGaugeChartModel criarModeloMedidorGas() {
		List<Number> marcadores = new ArrayList<Number>();
		marcadores.add(20);
		marcadores.add(40);
		marcadores.add(60);
		marcadores.add(80);
		marcadores.add(100);
		return new MeterGaugeChartModel(0, marcadores);
	}
	private MeterGaugeChartModel criarModeloMedidorChamas(){
		List<Number> marcadores = new ArrayList<Number>();
		marcadores.add(20);
		marcadores.add(40);
		marcadores.add(60);
		marcadores.add(80);
		marcadores.add(100);
		return new MeterGaugeChartModel (0,marcadores);
	}
	
	public MeterGaugeChartModel getModeloMedidorGas() {
		return modeloMedidorGas;
	}
	
	public MeterGaugeChartModel getModeloMedidorChamas(){
		return modeloMedidorChamas;
	}

	public void configurarGraficoPizza() {
		modeloGraficoPizza = new PieChartModel();
	}
	
	public PieChartModel getModeloGraficoPizza() {
		Informacao info = lerSensoresRetorno();

		modeloGraficoPizza.getData().put("Temperatura: ", info.getTemperatura());
		modeloGraficoPizza.getData().put("Detecção de Gas", info.getGas());
		modeloGraficoPizza.getData().put("Detecção Incêndio", info.getChamas());
		// Criar modelo
		modeloGraficoPizza.setSeriesColors("ff0000,ffff00,ff8000");
		modeloGraficoPizza.setTitle("Variação dos Sensores");
		modeloGraficoPizza.setLegendPosition("e");
		modeloGraficoPizza.setFill(false);
		modeloGraficoPizza.setDiameter(300);
		modeloGraficoPizza.setShowDataLabels(true);

		return modeloGraficoPizza;
	}
	
	public int segundoAtual(){
		return Calendar.getInstance().get(Calendar.SECOND);
	}
	
}