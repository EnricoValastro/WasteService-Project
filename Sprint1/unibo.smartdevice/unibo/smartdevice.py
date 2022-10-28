import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

hostAddress = "localhost"

def inserisci():
    port = int(input("Inserisci il numero di porta a cui connettersi\n"))
    connect(port)

def connect(p):
    server_address = (hostAddress, p)
    try:
        sock.connect(server_address)
        print("CONNECTED WITH", server_address)
    except:
        print("Si è verificato un errore. Riprova\n")
        inserisci()


def terminate():
    sock.close()
    print("BYE")


def request(message):
    print("request", message)
    msg = message + "\n"
    byt = msg.encode()  # required Python3
    sock.send(byt)
    handleAnswer()


def console():
    print('Inserisci il tipo di carico\n')
    mat = input("GLASS OR PLASTIC\n")
    if mat == "GLASS" or mat == "PLASTIC":
        print("Inserisci il peso del carico:\n")
        qua = input()
        storewaste = "msg(storewaste, request, smartdevice, wasteservicehandler, storewaste(MAT, QUA),1)"
        storewaste = storewaste.replace("MAT", mat)
        storewaste = storewaste.replace("QUA", qua)
        request(storewaste)
    else:
        print("Errore. Inserisci correttamente i dati della richiesta\n")
        console()


def handleAnswer():
    print("handleAnswer")
    while True:  ##client wants to maintain the connection
        reply = ''
        while True:
            answer = sock.recv(50)
            ## print("answer len=", len(answer))
            if len(answer) <= 0:
                break
            reply += answer.decode("utf-8")
            ## print("reply=", reply)
            if reply.endswith("\n"):
                break
        print("final reply=", reply)
        console()


inserisci()
console()
