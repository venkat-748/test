var bookdate;
var from;
var to;
var no_seat;
var train_id;
var price;
/* signup function */
async function signup() {
	console.log("hello peter");
	var email = document.getElementById("inputEmail").value;
	var pass = document.getElementById("inputPassword").value;
	var name = document.getElementById("userName").value;
	var phone = document.getElementById("phone").value;
	var datas = await fetch("http://localhost:8080/TicketReservation/Signup?email=" + email + "&pass=" + pass + "&name=" + name + "&phone=" + phone, { method: "POST" });
	var out = await datas.json();
	console.log(out.result);
	if (out.result == false) {
		var res = document.getElementById('result');
		res.innerText = 'Already Existing user';
	}
}

/* Train Available checking */
async function checking() {
	var date = document.getElementById("date").value;
	var start = document.getElementById("start").value;
	var end = document.getElementById("end").value;
	from = start;
	to = end;
	var datas = await fetch("http://localhost:8080/TicketReservation/Checking?start=" + start + "&end=" + end + "&date=" + date, { method: "POST" });
	var out = await datas.json();
	console.log(out);
	if (out.result == false) {
		console.log("hello peter");
		var res = document.getElementById('result');
		res.innerText = 'Invalid Details';
	}
	else {
		var arr = ["Train_id", "Seat_Avail", "Price", "Date", "Travel", "Start_Time", "Arrive", "fromPlace", "toPlace"];
		var total = document.getElementById("total");
		var information = document.getElementById("information");
		var date = document.getElementById("date").value;
		var start = document.getElementById("start").value;
		var end = document.getElementById("end").value;
		total.style.display = "none";
		information.style.display = "block";
		document.getElementById("information").innerHTML = "";
		var newTable = "<table border='1px solid white' width='100%'><tr>";
		for (j = 0; j < 9; j++) {
			newTable += "<td align='center'>" + arr[j] + "</td>";
		}
		var id = 1;
		for (let train in out) {
			newTable += "</tr><tr>";
			var k = 0;
			for (let property in out[train]) {
				newTable += "<td id = " + arr[k] + "" + id + " align='center'>" + out[train][property] + "</td>";
				k++;
			}
			document.getElementById("information").innerHTML = newTable;
			var a = document.createElement('p');
			a.onclick = "";
			a.innerText = "book";
			a.style.color = "blue";
		 	train_id = document.getElementById('Train_id' + id).innerText;
			let totalseat = document.getElementById('Seat_Avail' + id).innerText;
		    price = document.getElementById('Price' + id).innerText;
		    console.log(train_id,price);
			no_seat = totalseat;
			bookdate = date;
			console.log(no_seat, bookdate);

			a.setAttribute('onclick', "seat(" + train_id + "," + price + ")");
			document.getElementById("link").appendChild(a);
			id++;
		}
	}
}
var sleeper;
var twoAC;
var threeAC;
/* Arranging seats */
async function seat(train_id, price) {
	console.log(price);
	sleeper = 0.6 * no_seat;
	twoAC = sleeper + 0.25 * no_seat;
	threeAC = twoAC + 0.15 * no_seat;
	console.log(bookdate);
	document.getElementById('information').style.display = "none";
	document.getElementById('link').style.display = "none";
	var seats = [];
	console.log("to " + to);
	var datas = await fetch("http://localhost:8080/TicketReservation/SeatCheck?train_id=" + train_id + "&date=" + bookdate + "&start=" + from + "&end=" + to, { method: "POST" });
	var out = await datas.json();
	for (let j in out) {
		console.log(out[j]);
		seats.push(out[j]);
	}
	console.log(seats);
	var seat = document.getElementById('seats');
	var sleep = document.getElementById('sleeper');
	var two = document.getElementById('twoAC');
	var three = document.getElementById('threeAC');
	var seatscat = document.getElementById('seatscat');
	seat.style.display = "block";
	seat.style.display = "flex";
	sleep.style.display = "block";
	sleep.style.display = "flex";
	two.style.display = "block";
	two.style.display = "flex";
	three.style.display = "block";
	three.style.display = "flex";
	seatscat.style.display = "block";
	seatscat.style.display = "flex";
	for (let i = 1; i <= no_seat; i++) {
		if (seats.includes(i)) {
			if (i <= sleeper) {
				var d = document.createElement('div');
				d.setAttribute("class", "sleeper");
				d.setAttribute("id", i);
				d.style.height = '5vh';
				d.style.width = '4.5vw';
				d.style.border = "1px solid red";
				d.style.margin = "1vh"
				d.style.backgroundColor = "gray";
				d.innerText = "A" + i;
				sleep.appendChild(d);
			}
			else if (i <= twoAC) {
				var d = document.createElement('div');
				d.setAttribute("class", "twoAC");
				d.setAttribute("id", i);
				d.style.height = '5vh';
				d.style.width = '4.5vw';
				d.style.border = "1px solid red";
				d.style.margin = "1vh"
				d.style.backgroundColor = "gray";
				d.innerText = "A" + i;
				two.appendChild(d);
			}
			else {
				var d = document.createElement('div');
				d.setAttribute("class", "threeAC");
				d.setAttribute("id", i);
				d.style.height = '5vh';
				d.style.width = '4.5vw';
				d.style.border = "1px solid red";
				d.style.margin = "1vh"
				d.style.backgroundColor = "gray";
				d.innerText = "A" + i;
				three.appendChild(d);
			}
		}
		else {
			if (i <= sleeper) {
				var d = document.createElement('div');
				d.setAttribute("class", "0");
				d.setAttribute("id", i);
				d.setAttribute("onclick", "book(this.id)");
				d.style.height = '5vh';
				d.style.width = '4.5vw';
				d.style.border = "1px solid #1fe71f";
				d.style.margin = "1vh"
				d.style.backgroundColor = "#9BDDFF";
				d.innerText = "A" + i;
				sleep.appendChild(d);
			}
			else if (i <= twoAC) {
				var d = document.createElement('div');
				d.setAttribute("class", "0");
				d.setAttribute("id", i);
				d.setAttribute("onclick", "book(this.id)");
				d.style.height = '5vh';
				d.style.width = '4.5vw';
				d.style.border = "1px solid #1fe71f";
				d.style.margin = "1vh"
				d.style.backgroundColor = "#ED8179";
				d.innerText = "A" + i;
				two.appendChild(d);
			}
			else {
				var d = document.createElement('div');
				d.setAttribute("class", "0");
				d.setAttribute("id", i);
				d.setAttribute("onclick", "book(this.id)");
				d.style.height = '5vh';
				d.style.width = '4.5vw';
				d.style.border = "1px solid #1fe71f";
				d.style.margin = "1vh"
				d.style.backgroundColor = "#FFF8DC";
				d.innerText = "A" + i;
				three.appendChild(d);
			}
		}
	}

}
var button = document.createElement('button');
var classArr = [];
function book(id) {
	var element = document.getElementById(id);
	if (element.classList.contains("mystyle")) {
		if (id <= sleeper) {
			element.style.backgroundColor = "#9BDDFF";
		}
		else if (id <= twoAC) {
			element.style.backgroundColor = "#ED8179";
		}
		else {
			element.style.backgroundColor = "#FFF8DC";
		}
		element.classList.remove("mystyle");
		var index = classArr.indexOf(id);
		console.log(classArr.splice(index, 1));
		console.log(classArr);
	}
	else {
		element.setAttribute("class", "mystyle");
		element.style.backgroundColor = "red";
		classArr.push(id);
		console.log(classArr);
	}
	if(classArr.length==1){
		but();
	}
	else if(classArr.length==0){
		button.style.display="none";
	}
	console.log(classArr.length);
	}
	function but(){
	button.style.display="block";
	button.innerText = "pay";
	button.setAttribute('id', 'button');
	button.style.backgroundColor = "blue";
	console.log(classArr.length);
	if(classArr.length>0){
	button.setAttribute('onclick', "pay(" + train_id + "," + price + ")");
	}
	document.body.appendChild(button);
	}
	var ticketDetails = [];
/* Confirm Tickets */

function pay(train_id, price) {
	console.log(train_id, price);
	document.getElementById('seats').style.display = "none";
	document.getElementById('button').style.display = "none";
	let cash = document.getElementById('cash');
	cash.style.margin = "auto";
	cash.style.display = "block";
	let payment = document.getElementById('payment');
	let cost = 0;
	for (let j in classArr) {
		let i = classArr[j];
		price = i <= 60 ? price : i <= 80 ? (price + 0.25 * price) : (price + 0.5 * price);

		cost += price;
	}
	payment.innerText = "The total amount is :" + Math.round(cost);
	payment.style.color = "white";
	cash.appendChild(payment);
	let buy = document.getElementById('buy');
	buy.setAttribute("onclick", "proceed(" + train_id + ")");
}

async function proceed(train_id) {
	let cash = document.getElementById('cash');
	cash.style.display = "none";
	var user_id = ('; ' + document.cookie).split(`; user_id=`).pop().split(';')[0];
	var tickets = classArr.join();
	var datas = await fetch("http://localhost:8080/TicketReservation/TicketsBooking?train_id=" + train_id + "&tickets=" + tickets + "&date=" + bookdate + "&user_id=" + user_id + "&start=" + from + "&end=" + to, { method: "POST" });
	var out = await datas.json();
	console.log(out);
	for (let details in out) {
		for (let property in out[details]) {
			console.log(out[details][property]);
			ticketDetails.push(out[details][property]);
		}
	}
	document.getElementById('seats').style.display = "none";
	document.getElementById('button').style.display = "none";
	let success = document.getElementById('success');
	success.style.color = "green";
	success.style.display = "block";
	success.style.margin = "auto";
	let coaches = ticketDetails[3].split(',');
	let ticket = ticketDetails[2].split(',');
	let ticketString = "";
	for (let f = 0; f < ticket.length; f++) {
		let coachbox = coaches[f] == 1 ? "sleeper" : coaches[f] == 2 ? "2AC" : "3AC";
		ticketString += "A" + ticket[f] + "(" + coachbox + ") ";
	}
	console.log(ticketString);
	document.getElementById('details').innerText = "Ticket Details";
	document.getElementById('username').innerText = "TicketOwner: " + ticketDetails[0];
	document.getElementById('train').innerText = "Train_id: " + ticketDetails[1];
	document.getElementById('tick').innerText = "Tickets: " + ticketString;
	document.getElementById('fromPlace').innerText = "From: " + ticketDetails[4];
	document.getElementById('toPlace').innerText = "To: " + ticketDetails[5];
	document.getElementById('day').innerText = "Date: " + ticketDetails[6];
	document.getElementById('confirmTicket').setAttribute("onclick", "confirm(" + train_id + ")");
	document.getElementById('cancel').setAttribute("onclick", "cancel()");
}
async function confirm(train_id) {
	console.log(from, to);
	var tickets = classArr.join();
	console.log(('; ' + document.cookie).split(`; user_id=`).pop().split(';')[0]);
	var user_id = ('; ' + document.cookie).split(`; user_id=`).pop().split(';')[0];
	var datas = await fetch("http://localhost:8080/TicketReservation/InsertTicket?train_id=" + train_id + "&tickets=" + tickets + "&date=" + bookdate + "&user_id=" + user_id + "&start=" + from + "&end=" + to, { method: "POST" });
	var out = await datas.json();
	console.log(out);
	document.getElementById('username').innerText = "";
	document.getElementById('train').innerText = "";
	document.getElementById('tick').innerText = "";
	document.getElementById('fromPlace').innerText = "";
	document.getElementById('toPlace').innerText = "";
	document.getElementById('day').innerText = "";
	if (out.soln == false) {
		console.log("alreday booked");
		document.getElementById('seats').style.display = "none";
		document.getElementById('button').style.display = "none";
		let success = document.getElementById('success');
		success.style.color = "red";
		success.style.display = "block";
		success.style.margin = "auto";
		document.getElementById('confirmTicket').style.display = "none"
		document.getElementById('details').innerText = "This ticket is already booked";
		document.getElementById('thanks').innerText = "Try to book other tickets";
	}
	else {
		console.log(out);
		console.log(" booked your tickets");
		document.getElementById('seats').style.display = "none";
		document.getElementById('button').style.display = "none";
		let success = document.getElementById('success');
		success.style.color = "yellow";
		success.style.display = "block";
		success.style.margin = "auto";
		document.getElementById('confirmTicket').style.display = "none"
		document.getElementById('cancel').style.display = "none"
		document.getElementById('details').innerText = "Your ticket is booked";
		document.getElementById('thanks').innerText = "Start your happy journey";
	}
}
function cancel() {
	window.location.href = "TrainRegis.html";
}









