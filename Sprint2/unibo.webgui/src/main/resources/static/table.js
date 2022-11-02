const name = document.getElementById("ipaddr")

document.getElementById("connect").onclick = function() {
    name.classList.remove("text-danger");
    name.classList.add("text-success");
}