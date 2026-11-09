/**
 * Gray theme for Highcharts JS 2013.04.24
 * 
 * @author a2m
 * 
 */
var SVG_NS = 'http://www.w3.org/2000/svg';
var hasSVG = !!document.createElementNS && !!document.createElementNS(SVG_NS, 'svg').createSVGRect;

// chart2,chart3,chart4
var defaultOptions1 =  {colors: ['#7dbed0','#5c94ab','#bccc74','#619360','#fec200','#f06b6c','#685046']};
var defaultOptions2 =  {colors: ['#bccc74','#619360','#fec200','#f06b6c','#685046','#7dbed0','#5c94ab']};
var defaultOptions3 =  {colors: ['#fec200','#f06b6c','#685046','#7dbed0','#5c94ab','#bccc74','#619360']};
var defaultOptions4 =  {colors: ['#685046','#7dbed0','#5c94ab','#bccc74','#619360','#fec200','#f06b6c']};



var defaultColor=  ['#4572A7','#E47B28','#89A54E','#80699B','#3D96AE','#DB843D','#92A8CD','#A47D7C','#B5CA92'];


var defaultColor1=  ['#08369a','#2f5ec0','#3693e1','#06687a','#0c869d','#047667','#0c9b88','#3c7d07','#659104','#96be3d','#ffc232','#ff8932','#e33d03','#c22026','#743c14','#422108','#46390d','#8f6708','#6e3b97','#973b82','#ac1547','#d06ca4','#787878','#bbbbbb'];
var defaultColor2=  ['#2667b0','#5a99e0','#7dc3f9','#2292a7','#2fb2cb','#1f9a8a ','#2cb8a6','#429613','#87b325','#b0da51','#ffdb32','#ffad38','#f56915','#e2555a','#915b34','#693d1d','#6d5c21','#af8829','#8c4bc0','#bd5aa7','#cc3e6d','#e487bc','#949494','#d0d0d0'];

//var defaultColors=[{'color1':'#659104','color2':'#9ac43b'},{'color1':'#2f5ec0','color2':'#59a4e4'},{'color1':'#ffa200','color2':'#ffdd41'},{'color1':'#047667','color2':'#28ab9a'},{'color1':'#c22026','color2':'#eb5e63'},{'color1':'#6e3b97','color2':'#a45edc'},{'color1':'#3693e1','color2':'#7cc6ff'},{'color1':'#08369a','color2':'#3579c5'},{'color1':'#0c869d','color2':'#33bad3'},{'color1':'#06687a','color2':'#2ca4bb'},{'color1':'#3c7d07','color2':'#51ad1f'},{'color1':'#96be3d','color2':'#b9e459'},{'color1':'#0c9b88','color2':'#39c8b5'},{'color1':'#ff8932','color2':'#ffc438'},{'color1':'#e33d03','color2':'#f57b18'},{'color1':'#743c14','color2':'#af7449'},{'color1':'#422108','color2':'#7f4c27'},{'color1':'#46390d','color2':'#867331'},{'color1':'#8f6708','color2':'#c49c3c'},{'color1':'#973b82','color2':'#d170bb'},{'color1':'#ac1547','color2':'#da5581'},{'color1':'#d06ca4','color2':'#faa1d3'},{'color1':'#787878','color2':'#bbbbbb'},{'color1':'#bbbbbb','color2':'#e4e4e4'}]

var color1 = ['#2f5ec0','#659104','#ffa200','#047667','#c22026','#6e3b97','#3693e1','#08369a','#0c869d','#06687a','#3c7d07','#96be3d','#0c9b88','#ff8932','#e33d03','#743c14','#422108','#46390d','#8f6708','#973b82','#ac1547','#d06ca4','#787878','#bbbbbb']
var color2 = ['#59a4e4','#9ac43b','#ffdd41','#28ab9a','#eb5e63','#a45edc','#7cc6ff','#3579c5','#33bad3','#2ca4bb','#51ad1f','#b9e459','#39c8b5','#ffc438','#f57b18','#af7449','#7f4c27','#867331','#c49c3c','#d170bb','#da5581','#faa1d3','#bbbbbb','#e4e4e4']
//var defaultColors={'colors1':color1,'colors2':color2}

var color_main = ['#19b698','#7edc45'];
//var color_main = ['#4eb9c1','#fbdf79'];
//var color_chart = ['#198fb6','#19a5b6','#19b698','#7edc45','#c8dc45','#9baeb4'];
var color_chart = ['#c8dc45','#7edc45','#19b698','#19a5b6','#198fb6','#9baeb4'];
//var color_chart = ['#e33754','#ef954d','#efc74d','#a6da29','#54cadb','#718690'];


// y축이 두개일경우 DualyAxisColor1[0], DualyAxisColor1[1]
var DualyAxisColor1 = ['#7dbed0','#5c94ab'];
var DualyAxisColor2 = ['#bccc74','#619360'];
var point =function(obj){
	var aa = Math.round(obj,2);
	return obj;
} 

var setradialColor = function(){
	Highcharts.getOptions().colors = color1.concat([]);
	var colors = Highcharts.getOptions().colors;
	$.each(colors, function(i, color) {
	    colors[i] = {
	    		radialGradient: { cx: 0.4, cy: 0.5, r: 0.7 },
		        stops: [
		            
//		            [0, Highcharts.Color(color).brighten(0.3).get('rgb')], // darken
		            [0, color2[i]], // darken
		            [1, color]
		        ]
	    };
	});
	return colors;
}
var setlinearColor = function(){
	Highcharts.getOptions().colors = color1.concat([]);
	var colors = Highcharts.getOptions().colors;
	$.each(colors, function(i, color) {
	    colors[i] = {
	        linearGradient: { x1: 0, y1: 0, x2:0.7, y2: 0 },
	        stops: [
	            [0, color],
	            [0.3, color2[i]],
//	            [0.3, Highcharts.Color(color).brighten(0.7).get('rgb')],
	            [1, color]
	        ]
	    };
	});
	return colors;
};

var setlinearColor2 = function(colors, colors2){
	$.each(colors, function(i, color) {
	    colors[i] = {
	        linearGradient: { x1: 0, y1: 0, x2:0.7, y2: 0 },
	        stops: [
	            [0, color],
	            [0.3, colors2[i]],
//	            [0.3, Highcharts.Color(color).brighten(0.7).get('rgb')],
	            [1, color]
	        ]
	    };
	});
	return colors;
};

var radialColor=  setradialColor();
var linearColor=  setlinearColor();
/**
 * Default options
 */
Highcharts.theme = {
	
// colors:
// ['#7dbed0','#5c94ab','#bccc74','#619360','#fec200','#f06b6c','#685046'],
// colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572',
// '#FF9655', '#FFF263', '#6AF9C4'],
	colors: linearColor,
	symbols: ['circle'],
	lang: {
		spacingLeft:5,
		spacingRight:5,
		spacingTop:10,
		spacingBottom:5,			
		loading: 'Loading...',
		months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		shortMonths: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		weekdays: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
		decimalPoint: '.',
		numericSymbols: null,// ['k', 'M', 'G', 'T', 'P', 'E'], // SI
								// prefixes used in axis labels
		resetZoom: 'Reset zoom',
		resetZoomTitle: 'Reset zoom level 1:1',
		thousandsSep: ',',
		noData:''
	},
	global: {
		useUTC: true,
		canvasToolsURL: 'canvas-tools.js',
		VMLRadialGradientURL: 'vml-radial-gradient.png'
	},
	chart: {
		reflow:false,//window.resize시에 챠트 크기 변경하는 옵션 false로 바꿈(고정크기) - 성능향상기대 - zero
		backgroundColor:'#FFFFFF',
		borderColor: '#4572A7',
		borderRadius: 5,
		defaultSeriesType:'line',
		resetZoomButton: {
			position: {
				align: 'right',
				x: -10,
				y: 10
			},
			theme: {
				zIndex: 20
			}
		},
		style: {
			fontFamily: 'dotum, Arial, Verdana, Helvetica, sans-serif', // default font
			fontSize: '12px'
		}
	},
	title: {
		useHTML :true,
		style: {
			color: '#666',// #3E576F',
			fontSize: '12px',
			fontWeight: 'bold'
		},
		text: ''
	},
	subtitle: {
		style: {
			color: '#999'
		},
		text: ''
	},
	xAxis: {
		gridLineDashStyle: 'Dot',
		gridLineColor: '#ccc',
		gridLineWidth:0,
		lineWidth:1,
		lineColor:'#bbb',
		tickmarkPlacement:'on',
		tickWidth:0,
		//startOnTick:true,
		//endOnTick:true,
		//showFirstLabel:false,
		//showLastLabel:false,
		labels: {
			padding:1,
			style: {
				fontSize: '11px'
			}
		}
	},
	yAxis:  {
		allowDecimals: false,
		gridLineDashStyle: 'Dot',
		gridLineColor: '#ccc',
		gridLineWidth:1,
		lineWidth:1,
		lineColor:'#bbb',
		lineWidth:1,
		//startOnTick:false,
		//endOnTick:false,
		//showFirstLabel:false,
		//showLastLabel:false,
		title: { 
			text: ''
		},
		labels: {
            formatter: function() {
				return Highcharts.numberFormat(this.value, 0, ".", ","); 
            },
            style: {
				fontSize: '11px'
			}
			//,step:2
        },
        type: "linear"
        /*,tickPositioner:function(){
        	if(this.max != null && this.max != 0){
				var max = this.max ; 
				var positions = []; 
				var maxs = Math.round(max).toString().length
				if(maxs >= 1 ){
					var a = Math.floor(((max/Math.pow(10,maxs-1))-0.1)*2)/2+0.5;
        		}else {
        			var maxa = (max.toString().split('.')[1]).length;
        			var a = Math.floor(((max*Math.pow(10,maxa-1))-0.1)*2)/2+0.5;
					a= a/Math.pow(10,maxa-1)
        		}
				var maxu = a*Math.pow(10,maxs-1)
				var avg = maxu/10; 
				var increment = avg ;
				for(var i=0; i < 12 ;i++){ 
					positions[i] = increment * i;
				} 
        	}
			return positions;
		}*/
	},
	legend: {
		floating: false,
		verticalAlign: 'bottom',
		labelFormatter: function () {
			return this.name;
		},
		backgroundColor: '#FFFFFF',
		borderColor: '#CCC', 
		borderWidth: 1,
		shadow: false,
		navigation: {
			activeColor: '#000' // docs
		},
		itemMarginTop:3,
		itemMarginBottom:3 , 
		itemStyle: {
			cursor: 'pointer',
			color: '#666', // docs
			fontSize: '12px'
		},
		itemHoverStyle: {
			color: '#000'
		},
		itemHiddenStyle: {
			color: '#CCC'
		},
		itemCheckboxStyle: {
			position: 'absolute',
			width: '13px', // for IE precision
			height: '13px'
		},
		symbolWidth: 16,
		y: 10,
		title: { // docs
			style: {
				fontWeight: 'bold'
			}
		}	
	},
	labels: {
		style: {
			position: 'absolute',
			color: '#ff0000'
		}
	},
	navigation: {
		buttonOptions: {
			theme: {
				stroke: '#CCCCCC'
			}
		}
	},
	plotOptions: {
		area: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		arearange: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		areaspline: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		areasplinerange: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		bar: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		boxplot: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		bubble: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		column: {
			 pointPadding: 0.01,		//
             borderWidth: 0, 
             groupPadding:0.2,
             //pointWidth: 20,
             maxPointWidth:20,
			dataLabels: {
				color:'#666',
				formatter:function() {
					if(this.y <1 ){
						return Highcharts.numberFormat(this.y, 2, ".", ",");	
					}else{ 
						return Highcharts.numberFormat(this.y, 0, ".", ",");
					}
				} 
			},colors:linearColor
		},
		columnrange: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		errorbar: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		funnel: {
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		gauge :{
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		line: { // base series options
			lineWidth: 1,
			marker: {
				radius: 4,
				states: { // states for a single point
					select: {
//						fillColor: '#FFFFFF',
						lineWidth: 2
					}
				}
			},
			dataLabels: {
				
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		pie :{
			showInLegend: true,
			dataLabels: {
// connectorWidth:2,
				allowPointSelect: true,cursor: 'pointer',
				formatter:function() {
					//console.log( Math.round(this.percentage).toString().length )
// if( Math.round(this.percentage).toString().length >= 2)
					return Highcharts.numberFormat(this.percentage, 1, ".", ",")+'%';	  
				}
			},colors:radialColor
		},
		scatter :{
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		spline :{
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		},
		waterfall :{
			dataLabels: {
				formatter:function() {
					return Highcharts.numberFormat(this.y, 2, ".", ",");	
				}
			}
		}
	},

// loading: {
// labelStyle: {
// fontWeight: 'bold',
// position: 'relative',
// top: '1em'
// },
// style: {
// position: 'absolute',
// backgroundColor: 'white',
// opacity: 0.5,
// textAlign: 'center'
// }
// },
		tooltip: {
			dateTimeLabelFormats: { 
				millisecond: '%Y년 %m월 %d일 %H:%M:%S.%L',// '%A, %b %e,
														// %H:%M:%S.%L',//jcreator
				second: '%Y년 %m월 %d일 %H:%M:%S',// '%A, %b %e, %H:%M:%S'
				minute: '%Y년 %m월 %d일 %H:%M',// '%A, %b %e, %H:%M'
				hour: '%Y년 %m월 %d일 %H:%M',// '%A, %b %e, %H:%M'
				day: '%Y년 %m월 %d일 (%A)',// '%A, %b %e, %Y'
				week: '%Y년 %m월 %d일 (%A)',// 'Week from %A, %b %e, %Y'
				month: '%Y년 %m월',// '%B %Y'
				year: '%Y년'// '%Y'
			},
			valueDecimals:2,
			xDateFormat: '%Y년 %m월 %d일 %H:%M:%S',   
			headerFormat: '<span style="font-weight:bold;">{point.x}</span><br/>',
			pointFormat: '<span style="margin-top:30px;color:{series.color}">{series.name}</span>: {point.y}<br/>', 
			style: {
				color: '#666',   
				cursor: 'default',
				fontSize: '12px',
				padding: '6px', 
				whiteSpace: 'nowrap'
			},
			borderWidth: 1,
			backgroundColor: 'rgba(255,255,255,0.8)',//rgba(219,219,216,0.8)
			shadow: false,
			formatter:function(){
				var result ;
				if(this.percentage != null  ){ 
					result ='<span style="font-weight:bold;">'+this.key+'</span><br/>'
					+'<span style="margin-top:30px;color:{series.color}"><b>'+Highcharts.numberFormat(this.percentage, 1, ".", ",") +'%</b></span>';
					
				}else{ 
					if(typeof this.series !== "undefined"){
						result ='<span style="font-weight:bold;">'+this.series.name +'</span><br/>'
						+'<span style="margin-top:30px;color:{series.color}"><b>'+(this.key == "" ? '</b>':this.key+'</b> : ')+Highcharts.numberFormat(this.y, 0, ".", ",")+'</span>';
					}
				}
				return result;
			}
		},
	credits: {
		enabled: false
	},
	exporting: {
		enabled: true,
		buttons: {
			contextButton : {
				x: 5,
				y: -5,
				width: 11,
				height: 10,
				symbol:'menu',
				symbolSize: 8,
				symbolStrokeWidth:1,
				symbolStroke:'#aaa',
				simbolFill:'#aaa',
				symbolX: 5.5,
				symbolY: 5
			}
		}
	}
};
// '+useNotnull("{point.name}","{series.name}") +'
// Apply the theme
// 그래디언트 옵션


Highcharts.setOptions(Highcharts.theme);




function setMdmsChartOptions(){
	Highcharts.theme = {
	
			colors : defaultOptions1,
			symbols : [ 'circle' ],
			lang : {
				spacingLeft : 5,
				spacingRight : 5,
				spacingTop : 10,
				spacingBottom : 5,
				loading : 'Loading...',
				months : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월',
						'11월', '12월' ],
				shortMonths : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월',
						'10월', '11월', '12월' ],
				weekdays : [ '일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일' ],
				decimalPoint : '.',
				numericSymbols : null,// ['k', 'M', 'G', 'T', 'P', 'E'], // SI
										// prefixes used in axis labels
				resetZoom : 'Reset zoom',
				resetZoomTitle : 'Reset zoom level 1:1',
				thousandsSep : ','
			},
			global : {
				useUTC : true,
				canvasToolsURL : 'canvas-tools.js',
				VMLRadialGradientURL : 'vml-radial-gradient.png'
			},
			chart : {
				backgroundColor : '#FFFFFF',
				borderColor : '#4572A7',
				borderRadius : 5,
				defaultSeriesType : 'line',
				resetZoomButton : {
					position : {
						align : 'right',
						x : -10,
						y : 10
					},
					theme : {
						zIndex : 20
					}
				},
				style : {
					fontFamily : 'dotum, Arial, Verdana, Helvetica, sans-serif', // default
																					// font
					fontSize : '12px'
				}
			},
			title : {
				style : {
					color : '#222',// #3E576F',
					fontSize : '12px',
					fontWeight : 'bold'
				},
				text : ''
			},
			subtitle : {
				style : {
					color : '#222'
				},
				text : ''
			},
			xAxis : {
				tickmarkPlacement : 'on',
				lineWidth : 1,
				style : {
					color : '#222'
				}
			},
			yAxis : {
				lineWidth : 1,
				startOnTick : false,
				endOnTick : false,
				title : {
					text : ''
				}
			},
			legend : {
				verticalAlign : 'top',
				labelFormatter : function() {
					return this.name;
				},
				backgroundColor : '#FFFFFF',
				borderWidth : 0,
				navigation : {
					activeColor : '#000' // docs
				},
				itemStyle : {
					cursor : 'pointer',
					color : '#222', // docs
					fontSize : '12px'
				},
				itemHoverStyle : {
					color : '#000'
				},
				itemHiddenStyle : {
					color : '#CCC'
				},
				itemCheckboxStyle : {
					position : 'absolute',
					width : '13px', // for IE precision
					height : '13px'
				},
				symbolWidth : 16,
				y : 10,
				title : { // docs
					style : {
						fontWeight : 'bold'
					}
				}
			},
			labels : {
				style : {
					position : 'absolute',
					color : '#222222'
				}
			},
			navigation : {
				buttonOptions : {
					theme : {
						stroke : '#999999'
					}
				}
			},
			plotOptions : {
				area : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				arearange : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				areaspline : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				areasplinerange : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				bar : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				boxplot : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				bubble : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				column : {
					borderWidth : 0,
					pointWidth : 15,
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				columnrange : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				errorbar : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				funnel : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				gauge : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				line : { // base series options
					lineWidth : 1,
					marker : {
						radius : 4,
						states : { // states for a single point
							select : {
								fillColor : '#FFFFFF',
								lineWidth : 2
							}
						}
					},
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				pie : {
					shadow:false,
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				scatter : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				spline : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				},
				waterfall : {
					dataLabels : {
						formatter : function() {
							return Highcharts.numberFormat(this.y, 2, ".", ",");
						}
					}
				}
			},
	
			loading : {
				labelStyle : {
					fontWeight : 'bold',
					position : 'relative',
					top : '1em'
				},
				style : {
					position : 'absolute',
					backgroundColor : 'white',
					opacity : 0.5,
					textAlign : 'center'
				}
			},
			tooltip : {
				dateTimeLabelFormats : {
					millisecond : '%Y년 %m월 %d일 %H:%M:%S.%L',// '%A, %b %e,
															// %H:%M:%S.%L',//jcreator
					second : '%Y년 %m월 %d일 %H:%M:%S',// '%A, %b %e, %H:%M:%S'
					minute : '%Y년 %m월 %d일 %H:%M',// '%A, %b %e, %H:%M'
					hour : '%Y년 %m월 %d일 %H:%M',// '%A, %b %e, %H:%M'
					day : '%Y년 %m월 %d일 (%A)',// '%A, %b %e, %Y'
					week : '%Y년 %m월 %d일 (%A)',// 'Week from %A, %b %e, %Y'
					month : '%Y년 %m월',// '%B %Y'
					year : '%Y년'// '%Y'
				},
				valueDecimals : 2,
				xDateFormat : '%Y년 %m월 %d일 %H:%M:%S',
				headerFormat : '<span style="font-weight:bold;">{point.key}</span><br/>',
				pointFormat : '<span style="margin-top:30px;color:{series.color}">{series.name}</span>: {point.y}<br/>',
				style : {
					color : '#444444',
					cursor : 'default',
					fontSize : '12px',
					padding : '6px',
					whiteSpace : 'nowrap'
				},formatter:null
			},
			credits : {
				enabled : false
			}
		};
	
		// Apply the theme
	Highcharts.setOptions(Highcharts.theme);
}


function setMainChartOptions(){
	Highcharts.theme = {
			
			// colors:
			// ['#7dbed0','#5c94ab','#bccc74','#619360','#fec200','#f06b6c','#685046'],
			// colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572',
			// '#FF9655', '#FFF263', '#6AF9C4'],
				colors: linearColor, 
				symbols: ['circle'],
				lang: {
					spacingLeft:5,
					spacingRight:5,
					spacingTop:10,
					spacingBottom:5,			
					loading: 'Loading...',
					months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
					shortMonths: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
					weekdays: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
					decimalPoint: '.',
					numericSymbols: null,// ['k', 'M', 'G', 'T', 'P', 'E'], // SI
											// prefixes used in axis labels
					resetZoom: 'Reset zoom',
					resetZoomTitle: 'Reset zoom level 1:1',
					thousandsSep: ',',
					noData:''
				},
				global: {
					useUTC: true,
					canvasToolsURL: 'canvas-tools.js',
					VMLRadialGradientURL: 'vml-radial-gradient.png'
				},
				chart: {
					reflow:false,//window.resize시에 챠트 크기 변경하는 옵션 false로 바꿈(고정크기) - 성능향상기대 - zero
					backgroundColor:'#FFFFFF',
					borderColor: '#4572A7',
					borderRadius: 5,
					defaultSeriesType:'line',
					resetZoomButton: {
						position: {
							align: 'right',
							x: -10,
							y: 10
						},
						theme: {
							zIndex: 20
						}
					},
					style: {
						fontFamily: 'dotum, Arial, Verdana, Helvetica, sans-serif', // default font
						fontSize: '12px'
					}
				},
				title: {
					useHTML :true,
					style: {
						color: '#666',// #3E576F',
						fontSize: '12px',
						fontWeight: 'bold'
					},
					text: ''
				},
				subtitle: {
					style: {
						color: '#999'
					},
					text: ''
				},
				xAxis: {
					gridLineDashStyle: 'Dot',
					gridLineColor: '#ccc',
					gridLineWidth:0,
					lineWidth:1,
					lineColor:'#bbb',
					tickmarkPlacement:'on',
					tickWidth:0,
					//startOnTick:true,
					//endOnTick:true,
					//showFirstLabel:false,
					//showLastLabel:false,
					labels: {
						padding:1,
						style: {
							fontSize: '11px'
						}
					}
				},
				yAxis:  {
					allowDecimals: false,
					gridLineDashStyle: 'Dot',
					gridLineColor: '#ccc',
					gridLineWidth:1,
					lineWidth:1,
					lineColor:'#bbb',
					lineWidth:1,
					//startOnTick:false,
					//endOnTick:false,
					//showFirstLabel:false,
					//showLastLabel:false,
					title: { 
						text: ''
					},
					labels: {
			            formatter: function() {
							return Highcharts.numberFormat(this.value, 0, ".", ","); 
			            },
			            style: {
							fontSize: '11px'
						}
						//,step:2
			        },
			        type: "linear"
			        /*,tickPositioner:function(){
			        	if(this.max != null && this.max != 0){
							var max = this.max ; 
							var positions = []; 
							var maxs = Math.round(max).toString().length
							if(maxs >= 1 ){
								var a = Math.floor(((max/Math.pow(10,maxs-1))-0.1)*2)/2+0.5;
			        		}else {
			        			var maxa = (max.toString().split('.')[1]).length;
			        			var a = Math.floor(((max*Math.pow(10,maxa-1))-0.1)*2)/2+0.5;
								a= a/Math.pow(10,maxa-1)
			        		}
							var maxu = a*Math.pow(10,maxs-1)
							var avg = maxu/10; 
							var increment = avg ;
							for(var i=0; i < 12 ;i++){ 
								positions[i] = increment * i;
							} 
			        	}
						return positions;
					}*/
				},
				legend: {
					floating: false,
					verticalAlign: 'top',
					labelFormatter: function () {
						return this.name;
					},
					backgroundColor: '#FFFFFF',
					borderColor: '#CCC', 
					borderWidth: 1,
					shadow: false,
					navigation: {
						activeColor: '#000' // docs
					},
					itemMarginTop:3,
					itemMarginBottom:3 , 
					itemStyle: {
						cursor: 'pointer',
						color: '#666', // docs
						fontWeight:'normal'
					},
					itemHoverStyle: {
						color: '#000'
					},
					itemHiddenStyle: {
						color: '#CCC'
					},
					itemCheckboxStyle: {
						position: 'absolute',
						width: '13px', // for IE precision
						height: '13px'
					},
					symbolWidth: 16,
					y: 10,
					title: { // docs
						style: {
							fontWeight: 'bold'
						}
					}	
				},
				labels: {
					style: {
						position: 'absolute',
						color: '#ff0000'
					}
				},
				navigation: {
					buttonOptions: {
						theme: {
							stroke: '#CCCCCC'
						}
					}
				},
				plotOptions: {
					area: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					arearange: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					areaspline: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					areasplinerange: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					bar: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					boxplot: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					bubble: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					column: {
						 pointPadding: 0.01,		//
			             borderWidth: 0, 
			             groupPadding:0.2,
			             //pointWidth: 20,
			             maxPointWidth:20,
						dataLabels: {
							color:'#666',
							formatter:function() {
								if(this.y <1 ){
									return Highcharts.numberFormat(this.y, 2, ".", ",");	
								}else{ 
									return Highcharts.numberFormat(this.y, 0, ".", ",");
								}
							} 
						},colors:linearColor
					},
					columnrange: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					errorbar: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					funnel: {
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					gauge :{
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					line: { // base series options
						lineWidth: 1,
						marker: {
							radius: 4,
							states: { // states for a single point
								select: {
//									fillColor: '#FFFFFF',
									lineWidth: 2
								}
							}
						},
						dataLabels: {
							
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					pie :{
						showInLegend: true,
						dataLabels: {
			// connectorWidth:2,
							allowPointSelect: true,cursor: 'pointer',
							formatter:function() {
								//console.log( Math.round(this.percentage).toString().length )
			// if( Math.round(this.percentage).toString().length >= 2)
								return Highcharts.numberFormat(this.percentage, 1, ".", ",")+'%';	  
							}
						},colors:radialColor
					},
					scatter :{
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					spline :{
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					},
					waterfall :{
						dataLabels: {
							formatter:function() {
								return Highcharts.numberFormat(this.y, 2, ".", ",");	
							}
						}
					}
				},

			// loading: {
			// labelStyle: {
			// fontWeight: 'bold',
			// position: 'relative',
			// top: '1em'
			// },
			// style: {
			// position: 'absolute',
			// backgroundColor: 'white',
			// opacity: 0.5,
			// textAlign: 'center'
			// }
			// },
					tooltip: {
						dateTimeLabelFormats: { 
							millisecond: '%Y년 %m월 %d일 %H:%M:%S.%L',// '%A, %b %e,
																	// %H:%M:%S.%L',//jcreator
							second: '%Y년 %m월 %d일 %H:%M:%S',// '%A, %b %e, %H:%M:%S'
							minute: '%Y년 %m월 %d일 %H:%M',// '%A, %b %e, %H:%M'
							hour: '%Y년 %m월 %d일 %H:%M',// '%A, %b %e, %H:%M'
							day: '%Y년 %m월 %d일 (%A)',// '%A, %b %e, %Y'
							week: '%Y년 %m월 %d일 (%A)',// 'Week from %A, %b %e, %Y'
							month: '%Y년 %m월',// '%B %Y'
							year: '%Y년'// '%Y'
						},
						valueDecimals:2,
						xDateFormat: '%Y년 %m월 %d일 %H:%M:%S',   
						headerFormat: '<span style="font-weight:bold;">{point.x}</span><br/>',
						pointFormat: '<span style="margin-top:30px;color:{series.color}">{series.name}</span>: {point.y}<br/>', 
						style: {
							color: '#666',   
							cursor: 'default',
							fontSize: '12px',
							padding: '6px', 
							whiteSpace: 'nowrap'
						},
						borderWidth: 1,
						backgroundColor: 'rgba(255,255,255,0.8)',//rgba(219,219,216,0.8)
						shadow: false,
						formatter:function(){
							var result ;
							if(this.percentage != null  ){ 
								result ='<span style="font-weight:bold;">'+this.key+'</span><br/>'
								+'<span style="margin-top:30px;color:{series.color}"><b>'+Highcharts.numberFormat(this.percentage, 1, ".", ",") +'%</b></span>';
								
							}else{ 
								if(typeof this.series !== "undefined"){
									result ='<span style="font-weight:bold;">'+this.series.name +'</span><br/>'
									+'<span style="margin-top:30px;color:{series.color}"><b>'+(this.key == "" ? '</b>':this.key+'</b> : ')+Highcharts.numberFormat(this.y, 0, ".", ",")+'</span>';
								}
							}
							return result;
						}
					},
				credits: {
					enabled: false
				},
				exporting: {
					enabled: true,
					buttons: {
						contextButton : {
							x: 5,
							y: -5,
							width: 11,
							height: 10,
							symbol:'menu',
							symbolSize: 8,
							symbolStrokeWidth:1,
							symbolStroke:'#aaa',
							simbolFill:'#aaa',
							symbolX: 5.5,
							symbolY: 5
						}
					}
				}
			};
			// '+useNotnull("{point.name}","{series.name}") +'
			// Apply the theme
			// 그래디언트 옵션


			Highcharts.setOptions(Highcharts.theme);

}


