/*======================================
*			지원분야 도넛차트
=======================================*/
//empListJson은 JSP에서 가져온 JSON 문자열입니다.
var empListJson = '${empListJson}';
var empList = JSON.parse(empListJson);

// 필드 값들의 개수를 저장할 객체 생성
var fieldCounts = {};

// empList를 순회하며 field 값들의 개수를 증가
empList.forEach(function(emp) {
	var fieldString = emp.field.toString();

	if (fieldCounts.hasOwnProperty(fieldString)) {
		fieldCounts[fieldString]++;
	} else {
		fieldCounts[fieldString] = 1;
	}
});

// data 배열 동적 생성
var data = [];
for (var field in fieldCounts) {
	if (fieldCounts.hasOwnProperty(field)) {
		data.push({
			value: fieldCounts[field],
			name: field // 문자열로 저장된 값을 그대로 사용
		});
	}
}



var dom = document.getElementById('container1');
var myChart = echarts.init(dom, null, {
	renderer: 'canvas',
	useDirtyRect: false
});
var app = {};

var option;

option = {
	title: {  // 이 부분이 추가된 부분입니다.
		text: '지원분야',  // 제목 텍스트
		left: 'center',  // 가로 위치 (센터 정렬)
		top: '90%',  // 세로 위치 (5% 위치)
		textStyle: {
			fontSize: 25,  // 원하는 폰트 사이즈로 조절
			fontWeight: 'bold'
		}
	},
	tooltip: {
		trigger: 'item'
	},
	legend: {
		top: '5%',
		left: 'center'
	},
	series: [
		{
			name: '지원직무',
			type: 'pie',
			radius: ['40%', '70%'],
			avoidLabelOverlap: false,
			itemStyle: {
				borderRadius: 10,
				borderColor: '#fff',
				borderWidth: 2
			},
			label: {
				show: false,
				position: 'center'
			},
			emphasis: {
				label: {
					show: true,
					fontSize: 40,
					fontWeight: 'bold'
				}
			},
			labelLine: {
				show: false
			},
			data: data
		}
	]
};

if (option && typeof option === 'object') {
	myChart.setOption(option);
}

window.addEventListener('resize', myChart.resize);


