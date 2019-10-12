$( document ).ready(function() {
	
	var txnCountVsTxnTypeSeriesData = [];
	var txnTypeVsTxnAmountSeriesData = [];
	var txnType = [];
	var txnAmount =[];
	var txnCount = [];
	
	$.each(pageSummData, function(i, d){
		var tempArr = [];
		tempArr.push(d["txnName"]);
		tempArr.push(d["noOfTrns"]);
		txnType.push(d["txnName"]);
		txnAmount.push(d["noOfTrns"]);
		txnCount.push(parseFloat(d["sum"]));
		txnCountVsTxnTypeSeriesData.push(tempArr);
		
		var tempArr1 = [];
		tempArr1.push(d["txnName"]);
		tempArr1.push(parseFloat(d["sum"]));
		txnTypeVsTxnAmountSeriesData.push(tempArr1);
	});
	
	$(function () {
		
		
		 var chart = new Highcharts.Chart({
		        chart: {
		            renderTo: 'txnCountVsTxnType',
		            type: 'column',
		            options3d: {
		                enabled: true,
		                alpha: 15,
		                beta: 15,
		                depth: 50,
		                viewDistance: 25
		            }
		        },
	        title: {
	            text: 'Transaction Count Per Transaction Type'
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
	            categories: txnType
	        },
	        yAxis: {
	            title: {
	                text: null
	            }
	        },
	        series: [{
	            name: 'Transaction Count',
	            data: txnAmount
	        }]
	    });
		function showValues() {
	        $('#alpha-value').html(chart.options.chart.options3d.alpha);
	        $('#beta-value').html(chart.options.chart.options3d.beta);
	        $('#depth-value').html(chart.options.chart.options3d.depth);
	    }

	    // Activate the sliders
	    $('#sliders input').on('input change', function () {
	        chart.options.chart.options3d[this.id] = this.value;
	        showValues();
	        chart.redraw(false);
	    });

	    showValues();

	});

$(function () {
	
	
	 var chart = new Highcharts.Chart({
	        chart: {
	            renderTo: 'txnTypeVsTxnAmount',
	            type: 'column',
	            options3d: {
	                enabled: true,
	                alpha: 15,
	                beta: 15,
	                depth: 50,
	                viewDistance: 25
	            }
        },
        title: {
            text: 'Transaction Amount Per Transaction Type'
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
            categories: txnType
        },
        yAxis: {
            title: {
                text: null
            }
        },
        series: [{
            name: 'Transaction Amount',
            data: txnCount
        }]
    });
	function showValues() {
        $('#alpha-value').html(chart.options.chart.options3d.alpha);
        $('#beta-value').html(chart.options.chart.options3d.beta);
        $('#depth-value').html(chart.options.chart.options3d.depth);
    }

    // Activate the sliders
    $('#sliders input').on('input change', function () {
        chart.options.chart.options3d[this.id] = this.value;
        showValues();
        chart.redraw(false);
    });

    showValues();

});


});
