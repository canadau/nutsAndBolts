function showMessage(){
   alert("Hello World!");	
};

function timeNow(){
	var d = new Date();
	/*var n = d.getTimezoneOffset();*/
	document.getElementById("time").innerHTML = d;		
};
