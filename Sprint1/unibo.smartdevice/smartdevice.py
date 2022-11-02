import socket

"""def inserisci():
    address = input("Inserisci l'indirizzo IP del server\n")
    port = int(input("Inserisci porta a cui collegarsi\n"))
    connect(address, port)
"""

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


hostAddress = "localhost"
port = 8055


def connect(p):
    server_address = (hostAddress, p)
    sock.connect(server_address)
    print("CONNECTED WITH", server_address)


def request( message ) :
    msg = message + "\n"
    byt = msg.encode()    #required in Python3
    sock.send(byt)
    handleAnswer()

def console():
    print("Inserisci il tipo di carico:\n")
    mat = input("Glass or Plastic \n")
    print("Inserisci il peso del carico:\n")
    qua = input()
    storewaste = "msg(storewaste, request,smartdevice,wasteservice,storewaste(MAT,QUA),1)"
    storewaste = storewaste.replace("MAT", mat)
    storewaste = storewaste.replace("QUA", qua)
    request(storewaste)
    console()

def handleAnswer():
    while True: 
        reply = ''
        while True:
            answer = sock.recv(50)
            if len(answer) <= 0:
                break
            reply += answer.decode("utf-8")
            valuation = reply.replace("msg","")
            valuation = valuation.replace("(","")
            valuation = valuation.replace(")","")
            valuation = valuation.split(",")
            if reply.endswith("\n"):
                break
        if(valuation[0] == "loadaccept"):
            print("Carico accettato. Si prega di lasciare l'INDOOR")
        else:
            print("Carico rifiutato. Si prega di lasciare l'INDOOR")
        console()

console()
