let dataFromJson = {}

$.ajax({
    dataType: "json",
    url: "./WasteServiceSystemConfig.json",
    data: null,
    async: false,
    success: function(data) {
        $.each(data, function (k, v) {
            dataFromJson[k] = v
        })
    }
});

const home = dataFromJson.LOCATION.HOME
const indoor = dataFromJson.LOCATION.INDOOR
const plastic = dataFromJson.LOCATION.PLASTICBOX
const glass = dataFromJson.LOCATION.GLASSBOX


function generateTable() {
    let table = document.createElement("table")
    table.classList.add("table")
    table.classList.add("table-bordered")
    let tbody = document.createElement("tbody")
    table.appendChild(tbody)
    for (let i = 0; i < indoor[1]; i++) {
        let tr = document.createElement("tr")
        tr.style = "height: 40px;"
        for (let j = 0; j < plastic[0]; j++) {
            let td = document.createElement("td")
            td.style = "width: 40px; border:1px solid; background: #E3F2FD; text-align: center; vertical-align: middle;"
            tr.appendChild(td)
        }
        tbody.appendChild(tr)
    }
    let div = document.getElementById("table")
    div.appendChild(table);
}

function generateTableHome(){
    let table = document.createElement("table")
    table.classList.add("table")
    table.classList.add("table-bordered")
    let tbody = document.createElement("tbody")
    table.appendChild(tbody)
    for (let i = 0; i < indoor[1]; i++) {
        let tr = document.createElement("tr")
        tr.style = "height: 40px;";
        for (let j = 0; j < plastic[0]; j++) {
            let td=document.createElement("td")
            td.style="width: 40px; border:1px solid; background: #E3F2FD; opacity: 0.5; text-align: center; vertical-align: middle;"
            if(i==home[0] && j==home[1]) {
                td.style = "width:40px; border:1px solid; background-image: radial-gradient(circle 8px, white 99%, grey);"
            }
            tr.appendChild(td)
        }
        tbody.appendChild(tr)
    }
    let div = document.getElementById("table")
    div.appendChild(table);
}

function generateTableIndoor(){
    let table = document.createElement("table")
    table.classList.add("table")
    table.classList.add("table-bordered")
    let tbody = document.createElement("tbody")
    table.appendChild(tbody)
    for (let i = 0; i < indoor[1]; i++) {
        let tr = document.createElement("tr")
        tr.style = "height: 40px;";
        for (let j = 0; j < plastic[0]; j++) {
            let td=document.createElement("td")
            td.style="width: 40px; border:1px solid; background: #E3F2FD; opacity: 0.5; text-align: center; vertical-align: middle;"
            if(i==indoor[1]-1 && j==indoor[0]) {
                td.style = "width:40px; border:1px solid; background-image: radial-gradient(circle 8px, white 99%, blue);"
            }
            tr.appendChild(td)
        }

        tbody.appendChild(tr)
    }
    let div = document.getElementById("table")
    div.appendChild(table);
}

function generateTablePlastic(){
    let table = document.createElement("table")
    table.classList.add("table")
    table.classList.add("table-bordered")
    let tbody = document.createElement("tbody")
    table.appendChild(tbody)
    for (let i = 0; i < indoor[1]; i++) {
        let tr = document.createElement("tr")
        tr.style = "height: 40px;";
        for (let j = 0; j < plastic[0]; j++) {
            let td=document.createElement("td")
            td.style="width: 40px; border:1px solid; background: #E3F2FD; opacity: 0.5; text-align: center; vertical-align: middle;"
            if(i==plastic[1]-1 && j==plastic[0]-1) {
                td.style = "width:40px; border:1px solid; background-image: radial-gradient(circle 8px, white 99%, yellow);"
            }
            tr.appendChild(td)
        }

        tbody.appendChild(tr)
    }
    let div = document.getElementById("table")
    div.appendChild(table);
}

function generateTableGlass(){
    let table = document.createElement("table")
    table.classList.add("table")
    table.classList.add("table-bordered")
    let tbody = document.createElement("tbody")
    table.appendChild(tbody)
    for (let i = 0; i < indoor[1]; i++) {
        let tr = document.createElement("tr")
        tr.style = "height: 40px;";
        for (let j = 0; j < plastic[0]; j++) {
            let td=document.createElement("td")
            td.style="width: 40px; border:1px solid; background: #E3F2FD; opacity: 0.5; text-align: center; vertical-align: middle;"
            if(i==glass[1] && j==glass[0]-1) {
                td.style = "width:40px; border:1px solid; background-image: radial-gradient(circle 8px, white 99%, green);"
            }
            tr.appendChild(td)
        }

        tbody.appendChild(tr)
    }
    let div = document.getElementById("table")
    div.appendChild(table);
}
