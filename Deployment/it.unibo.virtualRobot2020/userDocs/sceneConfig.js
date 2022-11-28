const config = {
    floor: {
        size: { x: 31, y: 24                   }
    },
    player: {
        //position: { x: 0.5, y: 0.5 },		//CENTER
        position: { x: 0.10, y: 0.16 },		//INIT
        //position: { x: 0.8, y: 0.85 },		//END
        speed: 0.2
    },
    sonars: [
       ],
    movingObstacles: [
    ],
   staticObstacles: [
   /*
   
        {
            name: "plasticBox",
            centerPosition: { x: 0.15, y: 1.0},
            size: { x: 0.24, y: 0.07}
        },

		
        {
            name: "table",
            centerPosition: { x: 0.60, y: 0.40},
            size: { x: 0.16, y: 0.14      }
		},

        {
            name: "bottle1",
            centerPosition: { x: 0.55, y: 0.8 },
            size: { x: 0.05, y: 0.05      }
		},
		
        {
            name: "bottle2",
            centerPosition: { x: 0.18, y: 0.20},
            size: { x: 0.05, y: 0.05      }
		},


 
        {
            name: "obs1",
            centerPosition: { x: 0.05, y: 0.42 }, 
            size: { x: 0.053, y: 0.041}
        },

        {
            name: "obs2",
            centerPosition: { x: 0.94, y: 0.42 }, 
            size: { x: 0.053, y: 0.041}
        },
*/		 		 
        {
            name: "wallUp",
            centerPosition: { x: 0.44, y: 0.97},
            size: { x: 0.88, y: 0.01}
        },
        {
            name: "wallDown",
            centerPosition: { x: 0.44, y: 0.01},
            size: { x: 0.85, y: 0.01}
        },
        {
            name: "wallLeft",
            centerPosition: { x: 0.02, y: 0.48},
            size: { x: 0.01, y: 0.94}
        },
        {
            name: "wallRight",
            centerPosition: { x: 1.0, y: 0.5},
            size: { x: 0.01, y: 0.99}
        }
    ]
}

export default config;
