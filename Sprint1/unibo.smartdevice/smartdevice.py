import socket

hostAddress = "172.20.10.3"

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
    msg = message + "\n"
    byt = msg.encode()    #required in Python3
    sock.send(byt)
    handleAnswer()

def console():
    print("Inserisci il tipo di carico:\n")

    mat = input("Glass or Plastic \n")
    #if mat == "glass" or mat == "plastic".:
    print("Inserisci il peso del carico:\n")
    qua = input()
    storewaste = "msg(storewaste, request,smartdevice,wasteservice,storewaste(MAT,QUA),1)"
    #storewaste = "msg(storewaste, request,smartdevice,wasteservicehandler,storewaste(MAT,QUA),1)"
    storewaste = storewaste.replace("MAT", mat)
    storewaste = storewaste.replace("QUA", qua)
    request(storewaste)
    console()
    #else:
     #   print("Errore. Riprova a inserire i dati")
      #  console()

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

inserisci()
console()
