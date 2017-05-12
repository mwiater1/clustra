$(".button-collapse").sideNav();

var latchTemplate;

var state;

$.get('html/latchTemplate.html', function (template) {
    latchTemplate = template;
    Mustache.parse(latchTemplate)
});

var ws = new WebSocket("ws://" + window.location.hostname + "/ws");

ws.onopen = function () {
    console.log("WS OPENED!");
};

ws.onerror = function (p1) {
    console.log("WS ERROR!");
    $('#modal1').modal('open');
};

ws.onclose = function () {
    console.log("WS CLOSED!");
    $('#modal1').modal('open');
};

ws.onmessage = function (evt) {
    state = JSON.parse(evt.data);
    renderState();
};

function setLatchState(id, btnState) {
    state.latchViews[id].state = btnState;
    ws.send(JSON.stringify(state));
}

function setLatchName(id, obj) {
    state.latchViews[id].name = obj.innerHTML;
    ws.send(JSON.stringify(state));
}

function renderState() {
    $('#content').html(Mustache.render(latchTemplate, {latchViews: Object.values(state.latchViews)}));
}
