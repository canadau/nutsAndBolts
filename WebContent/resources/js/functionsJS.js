var item = 0;
var mode = getStoredValue('itemNumber');

$(document).ready(function(){
	
	var t = new Date();
	$("#timeNow").html("<br/>" + t );
	document.getElementById("item_number").innerHTML = item;
	
	
	
});

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

function getTimeNow(){

	var t = new Date();
	var x = document.getElementById("timeNow");
	$('#timeNow').html("The Time Was Saved <br/><br/>" + t );

}

