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
with Diagram('wasteservicesystemArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxpi', graph_attr=nodeattr):
          led=Custom('led','./qakicons/symActorSmall.png')
          sonar=Custom('sonar','./qakicons/symActorSmall.png')
     with Cluster('ctxrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxtruck', graph_attr=nodeattr):
          smartdevice=Custom('smartdevice','./qakicons/symActorSmall.png')
     with Cluster('ctxserver', graph_attr=nodeattr):
          wasteservicestatusgui=Custom('wasteservicestatusgui','./qakicons/symActorSmall.png')
     with Cluster('ctxwasteservice', graph_attr=nodeattr):
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     wasteservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='magenta', style='solid', xlabel='droppout', fontcolor='magenta') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid', xlabel='turnOn', fontcolor='blue') >> led
     transporttrolley >> Edge(color='blue', style='solid', xlabel='turnOff', fontcolor='blue') >> led
     transporttrolley >> Edge(color='blue', style='solid', xlabel='blink', fontcolor='blue') >> led
     transporttrolley >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     smartdevice >> Edge(color='magenta', style='solid', xlabel='storeWaste', fontcolor='magenta') >> wasteservice
diag
