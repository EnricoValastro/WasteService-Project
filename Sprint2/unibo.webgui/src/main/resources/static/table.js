// read local JSON file using jQuery
$.getJSON("wasteServiceSystemConfig.json", function (data) {
    let home = data['LOCATION']['HOME']
    let indoor = data['LOCATION']['INDOOR']
    let plastic = data['LOCATION']['PLASTICBOX']
    let glass = data['LOCATION']['GLASSBOX']

    let table = document.createElement("table")
    table.classList.add("table")
    table.classList.add("table-bordered")
    table.classList.add("table-dark")
    let tbody = document.createElement("tbody")
    table.appendChild(tbody)
    for (let i = 0; i < indoor[1]; i++) {
        let tr = document.createElement("tr")
        tr.style = "height: 40px;"
        for (let j = 0; j < plastic[0]; j++) {
            let td = document.createElement("td")
            td.style = "width: 40px; border:1px solid; background: #808080; opacity: 0.5; text-align: center; vertical-align: middle;"
            tr.appendChild(td)
        }
        tbody.appendChild(tr)
    }
    let div = document.getElementById("table")
    div.appendChild(table)
})


