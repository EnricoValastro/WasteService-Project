Introdotto il comando 
	remove( name )
che permette di rimuovere dalla scena un ostacolo fisso.

1) server.main.js activates TCPServer che invia comandi a  WebpageServer
2) WebpageServer definisce i comandi e emette per WebGLScene/js/SocketIO.js
3) SocketIO realizza il comando remove sulla GUI

remove usa le funzione updateSceneConstants 
definita in WebGLScene/js/utils/SceneConfigUtils.js che deve ricevere come primo argomento
la scena iniziale sceneConstants. 
Questo dato viene creato da WebGLScene/SceneManager.js 
che lo inietta  WebGLScene/js/utils/SceneConfigUtils.js
in modo che sia reperibile (via getinitialsceneConstants) da WebGLScene/js/SocketIO.js
(linea 73)