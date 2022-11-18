/*
PlayerControls
*/
import * as THREE from '../../node_modules/three/build/three.module.js'
import eventBus       from '../eventBus/EventBus.js'
import eventBusEvents from '../eventBus/events.js'

export default (mesh, camera, config, collisionManager, mirror) => {
	
    const keycodes = {
        W: 87,
        A: 65,
        S: 83,
        D: 68,
        R: 82,
        F: 70,
        Z: 90
    }
	
    let forward = false
    let backward = false
    let rotating = false
    var tween ;

    setCameraPositionRelativeToMesh(camera, mesh)

	function stopRotating(){ //April2022
//console.log("PlayerControls | stopRotatinggg tween=" + tween )
	    rotating = false
	    if( tween != undefined) tween.stop();
	}

    function onKeyDown(keyCode, duration, moveindex ) {
        //console.log("PlayerControls | onKeyDown  moveindex=" + moveindex + " keyCode=" + keyCode  )
        if(keyCode === keycodes.W) forward = true
        else if(keyCode === keycodes.S) backward = true
else if(keyCode === keycodes.Z) stopRotating() //April2022
        else if(keyCode === keycodes.D) rotate(-Math.PI/2, duration, moveindex)
        else if(keyCode === keycodes.A) rotate(Math.PI/2, duration, moveindex)
    }

    function onKeyUp(keyCode) {
        if(keyCode === keycodes.W) forward = false
        else if(keyCode === keycodes.S) backward = false;
    }

    function rotate(angle, duration = 300, moveindex) {
        //console.log("rotateee rotating="+rotating)
        duration -= 50
        if(rotating){  //April2020: la rotazione precedente ancora in corso inibisce la nuova
            console.log("PlayerControls | rotate  REJECTED"  )
            return
        }
        const finalAngle = mesh.rotation.y + angle
        rotating = true
        tween = new TWEEN.Tween(mesh.rotation)
            .to({ y: finalAngle }, duration)
            .easing(TWEEN.Easing.Quadratic.InOut)
            .onComplete( () => {
                rotating = false;
                eventBus.post(eventBusEvents.endmove, moveindex)
             })
            .start()
    }    

    function update(time) {
        const matrix = new THREE.Matrix4()
        matrix.extractRotation( mesh.matrix )

        const directionVector = new THREE.Vector3( 0, 0, -1 )
        directionVector.applyMatrix4(matrix)
    
	if(forward || backward) {
            const direction = backward ? 1 : -1
            const stepVector = directionVector.multiplyScalar( config.speed * direction )
            const tPosition  = mesh.position.clone().add(stepVector)
//console.log("PlayerControls | update timeeee =" + time)
            //if( !mirror ){
            	const collision = collisionManager.checkCollision(tPosition, mirror)		
            	if( !collision ) {
               		 mesh.position.add(stepVector)
                	 camera.position.add(stepVector)
             	}  
	   // }//mirror          
        } else 
            collisionManager.checkCollision(mesh.position)
    }
    
    function resetPosition() {
        mesh.position.x = config.position.x
        mesh.position.z = config.position.y

        setCameraPositionRelativeToMesh(camera, mesh)
    }

    function setCameraPositionRelativeToMesh(camera, mesh) {
        camera.position.x = mesh.position.x
        camera.position.z = mesh.position.z + 20

        camera.lookAt(new THREE.Vector3(mesh.position.x, 0, mesh.position.z))
    }
	
	return {
        resetPosition,
		onKeyDown,
		onKeyUp,
		update, stopRotating
	}
}