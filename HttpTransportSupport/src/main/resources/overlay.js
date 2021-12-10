/*
    This class enables dialogs above other web content. 
    The dialog has a titlebar, is movable, and closeable by the Windows window type X in the top right corner. 
    Closing the Overlay will dispose the rootElementForcontent.
    It is instantiated by:

    new Overlay(title, rootElementForContent, anyButtonOrOtherElementThatShouldBeDisabledWhileTheOverlayIsPresent);

    The OverlayManager enables access to a key listener to enable the Escape key to close the top most Overlay window.

    By Jörgen Damberg
*/

class OverlayManager {

    constructor(){
    }

    addEscapeKeyListenerToCloseTopMostOverlay(){
        document.addEventListener('keydown', function(evt){
            evt = evt || window.event;
            var isEscape = false;
            if ("key" in evt) {
                isEscape = (evt.key === "Escape" || evt.key === "Esc");
            } else {
                isEscape = (evt.keyCode === 27);
            }
            if (isEscape) {
                var allElements = document.querySelectorAll(".overlay");
                var topZNumber = 0;
                for(var i = 0; i < allElements.length; i++){
                    if(allElements[i].style.display == "none") continue;
                    var zIndex = getZIndex(allElements[i]);
                    if(zIndex > topZNumber) topZNumber = zIndex;
                }

                for(var u = 0; u < allElements.length; u++){
                    if(getZIndex(allElements[u]) >= topZNumber){
                        allElements[u].style.display = "none";
                    }
                }
            }
        } );
    }
}

class Overlay {

    constructor(title, contentRootElement, openButtonToDisableWhileVisible){
        this.window = document.createElement('div');
        this.window.addEventListener('click', function(){
            var overlays = document.querySelectorAll('.overlay');
            var topOverlayZindex = 0;
            for(var i = 0; i < overlays.length; i++){
                if(overlays[i].style.zIndex > topOverlayZindex && overlays[i].style.display != 'none') topOverlayZindex = overlays[i].style.zIndex;
            }
            this.window.style.zIndex = +topOverlayZindex + 1;
        }.bind(this), true);
        this.btn = openButtonToDisableWhileVisible;
        this.btn.disabled = true;
        this.window.classList.add('overlay');
        var overlays = document.querySelectorAll('.overlay');
        var topOverlayZindex = 0;
        for(var i = 0; i < overlays.length; i++){
            if(overlays[i].style.zIndex > topOverlayZindex && overlays[i].style.display != 'none') topOverlayZindex = overlays[i].style.zIndex;
        }
        this.window.style.zIndex = +topOverlayZindex + 1;
        var titlebar = document.createElement('div');
        titlebar.classList.add('titlebar');
        this.titletext = document.createElement('span');
        this.titletext.style.left = '0px';
        this.titletext.innerHTML = title;
        titlebar.appendChild(this.titletext);
        var closeX = document.createElement('span');
        closeX.classList.add('closeOverlayX');
        closeX.innerHTML = 'X';
        closeX.addEventListener('click', function(){
            this.btn.disabled = false;
            this.window.parentNode.removeChild(this.window);
        }.bind(this));
        titlebar.appendChild(closeX);
        titlebar.classList.add('overlayTitlebar');
        this.window.appendChild(titlebar);
        this.content = document.createElement('div');
        this.content.style = 'border: 1px solid black; background-color: white; overflow: auto;';
        this.content.appendChild(contentRootElement);
        this.window.appendChild(this.content);
        this.window.style.display = "block";
        document.querySelector('body').appendChild(this.window);
        this.dragElement(this.window);
    }

    dragElement(elmnt) {
        var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
        if (elmnt.querySelector('.titlebar')) {
            // if present, the header is where you move the DIV from:
                elmnt.querySelector('.titlebar').onmousedown = dragMouseDown;
        } else {
            // otherwise, move the DIV from anywhere inside the DIV:
            elmnt.onmousedown = dragMouseDown;
        }

        function dragMouseDown(e) {
            e = e || window.event;
            e.preventDefault();
            // get the mouse cursor position at startup:
            pos3 = e.clientX;
            pos4 = e.clientY;
            document.onmouseup = closeDragElement;
            // call a function whenever the cursor moves:
            document.onmousemove = elementDrag;
        }

        function elementDrag(e) {
            e = e || window.event;
            e.preventDefault();
            // calculate the new cursor position:
            pos1 = pos3 - e.clientX;
            pos2 = pos4 - e.clientY;
            pos3 = e.clientX;
            pos4 = e.clientY;
            // set the element's new position:
            elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
            elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
        }

        function closeDragElement() {
            // stop moving when mouse button is released:
            document.onmouseup = null;
            document.onmousemove = null;
        }
    }

}
