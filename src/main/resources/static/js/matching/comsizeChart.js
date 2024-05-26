var empListJson = '${empListJson}'; // JSP에서 문자열 그대로 가져옴
		
	    var empList = JSON.parse(empListJson);
		
	 	// comsize 값들의 개수를 저장할 객체 생성
	    var comsizeCounts = {};
		var comsizeCount=0;
	    // empList를 순회하며 comsize 값들의 개수를 증가
	    empList.forEach(function (emp) {
	    	comsizeCount++;
	      if (comsizeCounts.hasOwnProperty(emp.comsize)) {
	        comsizeCounts[emp.comsize]++;
	      } else {
	        comsizeCounts[emp.comsize] = 1;
	      }
	    });

	 	// comsizeCounts 객체를 사용하여 값들을 변수에 연결
	    var 중소 = comsizeCounts['중소'];
	    var 중견 = comsizeCounts['중견'];
	   	var 스타트업 = comsizeCounts['스타트업'];
	    var 대기업 = comsizeCounts['대기업'];
			 


	    google.charts.load("current", {packages:["corechart"]});
	    google.charts.setOnLoadCallback(drawChart);

	    function drawChart() {
	        // 새로운 배열을 만들어 comsizeCounts 객체의 값을 배열로 변환
	        var dataArray = Object.entries(comsizeCounts);

	        // 배열의 첫 번째 열을 추가하여 2차원 배열 형태로 만듦
	        dataArray.unshift(['Comsize', 'Count']);

	        var data = google.visualization.arrayToDataTable(dataArray);

	        var options = {
	            title: '회사 규모 (표본 :'+comsizeCount+'명)',
	            pieHole: 0.4,
	        };

	        var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
	        chart.draw(data, options);
	    }