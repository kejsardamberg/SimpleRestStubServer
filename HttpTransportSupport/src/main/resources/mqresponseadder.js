class MQResponseAdder{
    constructor(){
        this.rootElement = document.createElement('div');
        this.rootElement.id = "mqresponseadder";
        this.rootElement.classList.add('additiongui');
        var marginElement = document.createElement('div');
        marginElement.style.margin = "20px";
        var heading = document.createElement('h2');
        heading.innerHTML = "Message to send";
        marginElement.appendChild(heading);
        var contentLabel = document.createElement('label');
        contentLabel.for = "mqbody";
        contentLabel.innerHTML = "Message content:<br>";
        marginElement.appendChild(contentLabel);
        var contentArea = document.createElement('textarea');
        contentArea.id = "mqbody";
        contentArea.value = "";
        marginElement.appendChild(contentArea);
        marginElement.appendChild(document.createElement('br'));
        var heading2 = document.createElement('h2');
        heading2.innerHTML = "Filter (what triggers to send this message)";
        marginElement.appendChild(heading2);
        var topicLabel = document.createElement('label');
        topicLabel.innerHTML = "Topic:";
        topicLabel.for = "mqtopic";
        marginElement.appendChild(topicLabel);
        var mqtopic = document.createElement('input');
        mqtopic.type = "text";
        mqtopic.id = "mqtopic";
        mqtopic.value = "";
        marginElement.appendChild(mqtopic);
        marginElement.appendChild(document.createElement('br'));
        var submitButton = document.createElement('button');
        submitButton.innerHTML = "Submit";
        submitButton.disabled = true;
        marginElement.appendChild(submitButton);
        this.rootElement.appendChild(marginElement);
    }
}