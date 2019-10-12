$( document ).ready(function() {
	var txnCountVsTxnTypeSeriesData = [];
	var txnTypeVsTxnAmountSeriesData = [];
	var banks =[];
	var txnAmount = [];
	var txnCount = [];

	$.each(pageSummData, function(i, d){
		var tempArr = [];
		tempArr.push(d[0]);
		tempArr.push(d[1]);
		banks.push(d[0]);
		txnAmount.push(d[1]);
		txnCount.push(d[2]);
		txnCountVsTxnTypeSeriesData.push(tempArr);
		
		var tempArr1 = [];
		tempArr1.push(d[0]);
		tempArr1.push(parseFloat(d[2]));
		txnTypeVsTxnAmountSeriesData.push(tempArr1);
	});
	
	var pieChartDataOfBanksTxn = [];
	var pieChartDataOfBanksCount = [];
	$.each(pageSummData, function(i, d){
		var tempArr = {};
		tempArr['name'] = d[0];
		tempArr['y'] = d[1];
		pieChartDataOfBanksTxn.push(tempArr);
		var tempArr1 = {};
		tempArr['name'] = d[0];
		tempArr['y'] = d[2];
		pieChartDataOfBanksCount.push(tempArr);
	});
	
	if($("#txnCountVsTxnType").length > 0){
		Highcharts.chart('txnCountVsTxnType', {
	        chart: {
	            type: 'column',
	            options3d: {
	                enabled: true,
	                alpha: 10,
	                beta: 25,
	                depth: 70
	            }
	        },
	        title: {
	            text: 'Transaction Count Per Bank'
	        },
	        subtitle: {
	            text: ''
	        },
	        plotOptions: {
	            column: {
	                depth: 25
	            }
	        },
	        xAxis: {
	            categories: banks
	        },
	        yAxis: {
	            title: {
	                text: null
	            }
	        },
	        series: [{
	            name: 'Banks',
	            data: txnAmount
	        }]
	    });
	}

	if($("#txnTypeVsTxnAmount").length > 0){
		Highcharts.chart('txnTypeVsTxnAmount', {
	        chart: {
	            type: 'column',
	            options3d: {
	                enabled: true,
	                alpha: 10,
	                beta: 25,
	                depth: 70
	            }
	        },
	        title: {
	            text: 'Transaction Amount Per Bank'
	        },
	        subtitle: {
	            text: ''
	        },
	        plotOptions: {
	            column: {
	                depth: 25
	            }
	        },
	        xAxis: {
	            categories: banks
	        },
	        yAxis: {
	            title: {
	                text: null
	            }
	        },
	        series: [{
	            name: 'Banks',
	            data: txnCount
	        }]
	    });
}

	if($("#pieReportForTxn").length > 0){
		 Highcharts.chart('pieReportForTxn', {
		        chart: {
		            type: 'pie',
		            options3d: {
		                enabled: true,
		                alpha: 45,
		                beta: 0
		            }
		        },
		        title: {
		            text: ''
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                depth: 35,
		                dataLabels: {
		                    enabled: true,
		                    format: '{point.name}'
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: 'share',
		            data: txnCountVsTxnTypeSeriesData
		        }]
		    });
	}
});

