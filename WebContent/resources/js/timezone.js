function showMessage(){
   alert("Hello World!");	
};

function timeNow(){
	var buttonValue = document.getElementById("timeButton").value;
	
	/*if(buttonValue = "Save Time"){
		document.getElementById("timeButton").value="Get Time";
	};*/
	
	var t = new Date();
	var x = document.getElementById("savetime");
	var pValue = x.innerHTML;
	if (pValue == " ") {
		$('#savetime').html("The Time Was Saved <br/><br/>" + t );
		clearTime();
		
	} else {
		document.getElementById("savetime").style.display = "";
		$('#savetime').html( t );
	}

	
};

function clearTime(){
	var xe = document.getElementById("savetime");
	setTimeout(function(){ xe = document.getElementById("timeButton").value="Get Time"}, 2000);
	setTimeout(function(){ $("#savetime").hide(2000)}, 2000);
	setTimeout(function(){ $('#savetime0').html("Please click the button to get the time" )}, 2000);
	
};

function alertFunc() {
	  alert("Hello!");
	};

$("#ttt").click( function() {
	var myTime;
	var t = new Date();
	$("#savetime").text(t);
	myTime = setTimeout(alertFunc, 3000);
	myTime = setTimeout("clearTime()", 3000);
	
});
