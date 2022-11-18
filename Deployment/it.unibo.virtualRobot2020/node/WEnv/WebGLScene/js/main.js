/*
Scene
*/

import SceneManager from './SceneManager.js'
import initSocketIO from './SocketIO.js'
 
initSocketIO(onKeyUp, onKeyDownRemote)

const canvas = document.getElementById('canvas')
const sceneManager = SceneManager(canvas, false)	//false is mirror

bindEventListeners()
startRenderLoop()

function bindEventListeners() {
	window.onresize  = resizeCanvas
	window.onkeydown = onKeyDown
	window.onkeyup   = onKeyUp
	resizeCanvas()
}

function resizeCanvas() {
	canvas.style.width = '100%'
	canvas.style.height= '100%'
	
	canvas.width  = canvas.offsetWidth
	canvas.height = canvas.offsetHeight
    
    sceneManager.onWindowResize()
}

function onKeyDown(event, duration, moveIndex) {
    console.log("onKeyDown from main event.keyCode=" + event.keyCode + " moveIndex=" + moveIndex)
	sceneManager.onKeyDown(event.keyCode, duration, moveIndex)
}

function onKeyDownRemote(event, duration, moveIndex) {
    //console.log("onKeyDownRemote from main event.keyCode=" + event.keyCode + " moveIndex=" + moveIndex)
	sceneManager.onKeyDown(event.keyCode, duration, moveIndex)
}

function onKeyUp(event) {
	sceneManager.onKeyUp(event.keyCode)
}

function startRenderLoop(time) {
    requestAnimationFrame(startRenderLoop)
	sceneManager.update()
	TWEEN.update(time)
}

(function initPlugHTML() {
	const plugDiv = document.getElementById('plug')
	const meaninglessIcons = [
		document.getElementById('forklift-icon'),
		document.getElementById('lamp-icon'),
		document.getElementById('tractor-icon'),
		document.getElementById('washing-machine-icon'),
		document.getElementById('fork-icon'),
		document.getElementById('shovel-icon'),
		document.getElementById('carrot-icon'),
	]
	plugDiv.insertBefore(meaninglessIcons[getRandomInt(0, meaninglessIcons.length-1)], plugDiv.children[1])
})()