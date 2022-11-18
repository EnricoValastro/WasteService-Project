##############################################################
# basicRobotCaller.py
##############################################################
import socket
import time

port       = 8020
sock       = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
calledName = "basicrobot"

cmd         = "step(350)"
requestMsg  = "msg(step,request,python,RECEIVER,PAYLOAD,1)".replace("RECEIVER",calledName).replace("PAYLOAD",cmd)
alarm       = "msg(alarm,event,python,none,alarm(fire),3)"

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED " , server_address)

def sendMsg( message ) :
    print("sendMsg ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def handleAnswer() :
    print("handleAnswer " )
    while True:  ##client wants to maintain the connection
        reply = ''
        while True:
            answer = sock.recv(50)
            ## print("answer len=", len(answer))
            if len(answer) <= 0 :
                break
            reply += answer.decode("utf-8")
            ## print("reply=", reply)
            if reply.endswith("\n") :
                break
        print("final reply=", reply)
        break
    	
def emit( event ) :
    print("EMIT ", event)
    msg = event + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    sendMsg( requestMsg ) 
    handleAnswer()
    ##time.sleep(1.5)
    ##emit( alarm )


def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
##terminate()  
