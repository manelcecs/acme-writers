function addComment(insertPlace, name, clase) {
	var x = document.getElementById(insertPlace);
	var input1 = document.createElement("input");
	input1.setAttribute("type", "text");
	input1.setAttribute("class", clase);
	input1.setAttribute("name", name);
	input1.setAttribute("id", name);
	x.appendChild(input1);
}
