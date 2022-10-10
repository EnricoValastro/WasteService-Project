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
     with Cluster('ctxwasteservice', graph_attr=nodeattr):
          containermanager=Custom('containermanager','./qakicons/symActorSmall.png')
          wasteservicehandler=Custom('wasteservicehandler','./qakicons/symActorSmall.png')
          wasteservicecore=Custom('wasteservicecore','./qakicons/symActorSmall.png')
     with Cluster('ctxtransporttrolley', graph_attr=nodeattr):
          transportrolleycore=Custom('transportrolleycore(ext)','./qakicons/externalQActor.png')
     wasteservicehandler >> Edge(color='magenta', style='solid', xlabel='evalreq', fontcolor='magenta') >> containermanager
     wasteservicehandler >> Edge(color='blue', style='solid', xlabel='doJob', fontcolor='blue') >> wasteservicecore
     wasteservicecore >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transportrolleycore
     wasteservicecore >> Edge(color='blue', style='solid', xlabel='dropout', fontcolor='blue') >> transportrolleycore
diag
