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
with Diagram('testwasteserviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxwasteservice', graph_attr=nodeattr):
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolleymover=Custom('transporttrolleymover','./qakicons/symActorSmall.png')
          transporttrolleycore=Custom('transporttrolleycore','./qakicons/symActorSmall.png')
          transporttrolleyexecutor=Custom('transporttrolleyexecutor','./qakicons/symActorSmall.png')
          systemstatemanager=Custom('systemstatemanager','./qakicons/symActorSmall.png')
     sys >> Edge(color='red', style='dashed', xlabel='local_dropoutdone', fontcolor='red') >> wasteservice
     wasteservice >> Edge(color='blue', style='solid', xlabel='updatecontainer', fontcolor='blue') >> systemstatemanager
     wasteservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolleycore
     wasteservice >> Edge(color='blue', style='solid', xlabel='dropout', fontcolor='blue') >> transporttrolleycore
     wasteservice >> Edge(color='blue', style='solid', xlabel='gotohome', fontcolor='blue') >> transporttrolleycore
     wasteservice >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> systemstatemanager
diag
