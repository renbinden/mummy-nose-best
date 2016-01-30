// it is possible to add a whack a mole version except with flies instead of moles to stage 2 organ removal
// then youd have to hit the flies with 3 lives or something in a 20s time span


/*

	This document is licensed as free software under the terms of the
	MIT License: http://www.opensource.org/licenses/mit-license.php 
	
*/


var whackamole = whackamole || (function(window, undefined) {
	
	// booleans, ints, and timers oh my!
	var game, score, popping, startTime, currentTime, clicked, moles, gameTimeout, hits = 0;
	
	
	var	liveClass = "wam-pesky-mole",
		deadClass = "wam-pesky-mole-dead",
		hidingInterval = 1500,
		poppingInterval = 750,
		moleLimit = 10;
		

	function getStyle(el, cssprop){
		if (el.currentStyle) {
			return el.currentStyle[cssprop];
		} else if (document.defaultView && document.defaultView.getComputedStyle) {
			return document.defaultView.getComputedStyle(el, "")[cssprop];
		}
	}

	game = {
		mode: "start",
	
		live: function() {
			this.mole.className = liveClass;
			this.mole.clicked = false;
			this.mode = "main";
		},
		
		kill: function() {
			var currentTime = (new Date).getTime();
			score += (Math.floor( ( ( poppingInterval - (currentTime - startTime) ) / poppingInterval) * 100 )) * 10;
			hits++;
			this.mole.className = deadClass;
			this.mode = "dead";
		},
		move: function() {
			moles++;
			clicked = false;
			this.mole.style.top = Math.floor(Math.random() * (parseInt(getStyle(this.stage, "height")) - parseInt(getStyle(this.mole, "height")) ) ) + "px";
			this.mole.style.left = Math.floor(Math.random() * (parseInt(getStyle(this.stage, "width")) - parseInt(getStyle(this.mole, "width")) ) ) + "px";
			startTime = (new Date).getTime();
		},
		togglePop: function() {
			this.mole.style.display = (popping) ? "block" : "none";
		},
		reset: function() {
			game.mode = "main";
			popping = false;
			hits = score = moles = 0;
		},
	
		showStart: function() {
			this.startScreen.style.display = "block";
		},
		showScoreboard: function() {
			this.sb.style.display = "block";
		}
	}
	
	
	function setup(elementId) {
		
		var mole, sb, stage;
		
		// the mole
		mole = game.mole = document.createElement("div");
		mole.className = liveClass;
		mole.style.display = "none";
		// who needs cross-browser event handling?
		mole.onclick = function() {
			if (!game.mole.clicked) {
				game.kill();
				game.mole.clicked = true;
				game.scoreboard.update();
				window.clearTimeout(gameTimeout);
				step();
			}
		};
		
		// the scoreboard
		sb = game.scoreboard = document.createElement("div");
		sb.className = "wam-scoreboard";
		sb.update = function() {
			this.innerHTML = "points: " + score + "<br />Moles: " + hits + " / " + moles;
		}
		
		// the start screen
		ss = game.startScreen = document.createElement("div");
		ss.className = "wam-startScreen";
		ss.innerHTML = "start";
		ss.style.display = "none";
		ss.onclick = function() {
			game.mode = "main";
			this.style.display = "none";
			step();
		}
		
	
		es = game.endScreen = document.createElement("div");
		es.className = "wam-endScreen";
		es.style.display = "none";
		es.innerHTML = "Play again?";
		es.onclick = function() {
			game.reset();
			game.mode = "main"
			this.style.display = "none";
			game.startScreen.display = "none";
			step();
			
		}
		
	
		stage = game.stage = document.getElementById(elementId);
		stage.style.position = "relative";
	
		stage.appendChild(ss);
		stage.appendChild(sb);
		stage.appendChild(mole);
		stage.appendChild(es);
		
	}
	

	function step() {
		switch(game.mode) {
			case "start":
				game.showStart();
				break;
			case "dead":
				gameTimeout = setTimeout(function(){
					
					step();
				}, 500);
				game.mode = "main";
				break;
			case "main":
				game.scoreboard.update();
				if (moles >= moleLimit) {
					game.mode = "end";
					gameTimeout = setTimeout(step, 10);
					break;
				}
				game.live();
				game.togglePop();
				if (popping) game.move();
				popping = (popping) ? false : true;
				gameTimeout = setTimeout(step, (popping) ? hidingInterval : poppingInterval);
				break;
			case "end":
			default:
				game.scoreboard.innerHTML = "Final Score: " + score + "<br />Moles: " + hits + " / " + moles;
				game.endScreen.style.display = "block";
				break;
		}
	}
	

	return {
		setup: function(element) {
			setup(element);
			this.start();
		},
		start: function() {
			game.reset();
			game.mode = "start";
			step();
		},
		stop: function() {
			game.mode = "dead";
			moles = moleLimit + 1;
			window.clearTimeout(gameTimeout);
			step();
		}
	};
	
})(window);
