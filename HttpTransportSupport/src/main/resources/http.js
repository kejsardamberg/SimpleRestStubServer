class HttpHeader {
    constructor(name, value){
        this.name = name;
        this.value = value;
    }
}

class HttpMessage {
    constructor(){
        this.body = null;
        this.headers = [];
    }
}



class HttpRequest extends HttpMessage {
    constructor(verb, endpoint, body, headers){
        super();
        this.endpoint = endpoint;
        this.verb = verb;
        this.body = body;
        for(var i = 0; i < headers.length; i++){
            this.headers.push(headers[i]);
        }
    }
}

class HttpResponse extends HttpMessage {
    constructor(){
        super();
        this.statusCode = 0;
    }
}

class PreparedHttpResponse extends HttpResponse {
    constructor(delayInMilliseconds){
        super();
        this.delayInMilliseconds = delayInMilliseconds;
        this.id = uuidv4();
        this.httpResponse = {};
        this.httpResponse.headers = [];
        this.filters = [];

    }    
}

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}


class PreparedHttpResponsesTable {
    constructor(attachParentElement){
        this.table = document.createElement('table');
        this.table.id = 'registeredResponsesTable';
        this.table.innerHTML = '<tr><th>Trigger(s)</th><th>Header(s)</th><th>Body</th><th style="text-align: center;">Status<br>code</th><th style="text-align: center;">Delay<br>(ms)</th><th></th></tr>';
        attachParentElement.appendChild(this.table);
        this.update();
    }

    update() {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                var trs = document.querySelectorAll('#registeredResponsesTable tr');
                for (var j = 1; j < trs.length; j++) {
                    trs[j].parentNode.removeChild(trs[j]);
                }
                console.log("Last response:\n\rResponse code: " + this.status + "\n\rResponse body content:\n\r" + this.responseText + "\n\r");
                var json = JSON.parse(this.responseText);
                var registeredResponses = json.registeredResponses;
                for (var i = 0; i < registeredResponses.length; i++) {
                    var tr = document.createElement('tr');
                    tr.setAttribute('id', registeredResponses[i].id);
                    var trigger = document.createElement('td');
                    trigger.classList.add('triggerscell');
                    var triggers = registeredResponses[i].filters;
                    var triggerstrings = [];
                    if(triggers !== null){
                        for(var s = 0; s < triggers.length; s++){
                            triggerstrings.push(triggers[s].description);
                        }
                    }
                    trigger.innerHTML = triggerstrings.join("<br>");
                    tr.appendChild(trigger);
                    var headers = document.createElement('td');
                    headers.classList.add('headerscell');
                    var headerstrings = [];
                    if(registeredResponses[i].httpResponse !== null && registeredResponses[i].httpResponse.headers !== null){
                        for(var y = 0; y < registeredResponses[i].httpResponse.headers.length ; y++){
                            headerstrings.push(registeredResponses[i].httpResponse.headers[y].name + ": " + registeredResponses[i].httpResponse.headers[y].value );
                        }
                    }
                    headers.innerHTML = headerstrings.join("<br>");
                    tr.appendChild(headers);
                    var body = document.createElement('td');
                    body.classList.add('bodycell');
                    body.classList.add('body');
                    var bodyText = textToHtml(registeredResponses[i].httpResponse.body);
                    if(bodyText === null || bodyText.length === 0){
                        bodyText =  textToHtml(registeredResponses[i].httpResponse.bodyFilePath);
                    }
                    var displayText = "<i>" + registeredResponses[i].httpResponse.bodyType + "</i>: " + bodyText;
                    if (displayText.length > 120) {
                        displayText = displayText.substring(0, 117) + '...';
                    }
                    body.innerHTML = displayText;
                    tr.appendChild(body);
                    var statusCode = document.createElement('td');
                    statusCode.classList.add('statuscodecell');
                    statusCode.innerHTML = registeredResponses[i].httpResponse.responseCode;
                    tr.appendChild(statusCode);
                    var delay = document.createElement('td');
                    delay.classList.add('delaycell');
                    delay.innerHTML = registeredResponses[i].delay;
                    tr.appendChild(delay);
                    var buttonCell = document.createElement('td');
                    var editButton = document.createElement('img');
                    editButton.classList.add('tableicon');
                    editButton.src = penImage;
                    editButton.setAttribute('onclick', 'editHttpResponse(this)');
                    buttonCell.appendChild(editButton);
                    var deleteButton = document.createElement('img');
                    deleteButton.classList.add('tableicon');
                    deleteButton.src = trashbinImage;
                    deleteButton.setAttribute('onclick', 'deleteEntry(this);');
                    buttonCell.appendChild(deleteButton);
                    tr.appendChild(buttonCell);
                    document.querySelector('#registeredresponses table').appendChild(tr);
                }
            }
        };
        xhttp.open("GET", "/api", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send();
    }
}

class PreparedResponseEditGui {
    constructor(preparedHttpResponse){
        if(preparedHttpResponse == null){
            preparedHttpResponse = {};
        }
    }
}

