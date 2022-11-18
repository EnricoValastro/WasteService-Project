##############################################################
# pathexecCaller.py
##############################################################
import socket
import time

port       = 8020
sock       = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
calledName = "pathexec"

##PathTodo    = "'{\"path\":\"lrwwwwwwww\" , \"owner\":\"pathexecCaller\"}'"
PathTodo	= "dopath(\"wlwwlwlwwl\")"
requestMsg  = "msg(dopath,request,pythoncaller,RECEIVER,PAYLOAD,1)".replace("RECEIVER",calledName).replace("PAYLOAD",PathTodo)
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

def emit( event ) :
    print("EMIT ", event)
    msg = event + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    sendMsg( requestMsg ) 
    handleAnswer()

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

def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
##terminate()  
