import socket

hostAddress = "192.168.1.4"

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def inserisci():
    port = int(input("Inserisci porta a cui collegarsi\n"))
    connect(port)

def connect(p):
    server_address = (hostAddress, p)
    try:
        sock.connect(server_address)
        print("CONNECTED WITH", server_address)
    except:
        print("Si è verificato un errore. Riprova")
        inserisci()

def request( message ) :
    print("request", message)
    msg = message + "\n"
    byt = msg.encode()    #required in Python3
    sock.send(byt)
    handleAnswer()

def console():
    print("Inserisci il tipo di carico:\n")
    mat = input("GLASS OR PLASTIC \n")
    if mat == "GLASS" or mat == "PLASTIC":
        print("Inserisci il peso del carico:\n")
        qua = input()
        storewaste = "msg(storewaste, request,smartdevice,wasteservice,storewaste(MAT,QUA),1)"
        storewaste = storewaste.replace("MAT", mat)
        storewaste = storewaste.replace("QUA", qua)
        request(storewaste)
    else:
        print("Errore. Riprova a inserire i dati")
        console()

def handleAnswer():
    print("handleAnswer")
    while True:  ##client wants to maintain the connection
        reply = ''
        while True:
            answer = sock.recv(50)
            if len(answer) <= 0:
                break
            reply += answer.decode("utf-8")
            if reply.endswith("\n"):
                break
        print("reply", reply)
        console()

inserisci()
console()
