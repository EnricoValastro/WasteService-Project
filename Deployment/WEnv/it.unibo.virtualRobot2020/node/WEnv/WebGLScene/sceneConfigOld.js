const config = {
    floor: {
        size: { x: 25, y: 20 }
    },
    player: {
        //position: { x: 0.5, y: 0.5 },		//CENTER
        position: { x: 0.15, y: 0.3 },		//INIT
        //position: { x: 0.8, y: 0.85 },		//END
        speed: 0.2
    },
    sonars: [
        {
            name: "sonar1",
            position: { x: 0.12, y: 0.05 },
            senseAxis: { x: false, y: true }
        },
        {
            name: "sonar2",
            position: { x: 0.94, y: 0.88},
            senseAxis: { x: true, y: false }
        } 
     ],
    movingObstacles: [
        { 
            name: "moving-obstacle",
            position: { x: .5, y: .5 },
            directionAxis: { x: true, y: false },
            speed: 0.17,
            range: 30
        },
//        {
//            name: "movingobstacle",
//            position: { x: .5, y: .2 },
//            directionAxis: { x: true, y: true },
//            speed: 2,
//            range: 2
//        }
    ],
    staticObstacles: [
        {
            name: "wall",
            centerPosition: { x: 0.5, y: 0.9},
            size: { x: 0.1, y: 0.1}
        },

        {
            name: "wall",
            centerPosition: { x: 0.6, y: 0.7},
            size: { x: 0.1, y: 0.1}
        },
        {
        name: "wallUp",
        centerPosition: { x: 0.58, y: 0.98},
        size: { x: 0.8, y: 0.01}
        },
         {
            name: "wallDown",
            centerPosition: { x: 0.45, y: 0.01},
            size: { x: 0.85, y: 0.01}
        },
       {
            name: "wallRight",
            centerPosition: { x: 0.05, y: 0.4},
            size: { x: 0.01, y: 0.6}
        },
        {
            name: "wallLeft",
            centerPosition: { x: 0.9, y: 0.5},
            size: { x: 0.01, y: 0.65}
        }
    ]
}

export default config;
