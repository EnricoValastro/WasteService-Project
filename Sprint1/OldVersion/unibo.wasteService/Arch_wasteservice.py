from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('wasteserviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxtransporttrolley', graph_attr=nodeattr):
          transporttrolleycore=Custom('transporttrolleycore(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxwasteservice', graph_attr=nodeattr):
          containermanager=Custom('containermanager','./qakicons/symActorSmall.png')
          wasteservicehandler=Custom('wasteservicehandler','./qakicons/symActorSmall.png')
          wasteservicecore=Custom('wasteservicecore','./qakicons/symActorSmall.png')
     wasteservicehandler >> Edge(color='magenta', style='solid', xlabel='storewaste', fontcolor='magenta') >> wasteservicehandler
     wasteservicehandler >> Edge(color='magenta', style='solid', xlabel='evalreq', fontcolor='magenta') >> containermanager
     wasteservicehandler >> Edge(color='blue', style='solid', xlabel='update', fontcolor='blue') >> containermanager
     wasteservicehandler >> Edge(color='blue', style='solid', xlabel='dojob', fontcolor='blue') >> wasteservicecore
     sys >> Edge(color='red', style='dashed', xlabel='dropoutdone', fontcolor='red') >> wasteservicecore
     wasteservicecore >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolleycore
     wasteservicecore >> Edge(color='blue', style='solid', xlabel='dropout', fontcolor='blue') >> transporttrolleycore
     wasteservicecore >> Edge(color='blue', style='solid', xlabel='gotohome', fontcolor='blue') >> transporttrolleycore
     wasteservicecore >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> wasteservicehandler
     wasteservicecore >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> containermanager
diag
