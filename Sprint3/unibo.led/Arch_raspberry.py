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
with Diagram('raspberryArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxwasteservice', graph_attr=nodeattr):
          systemstatemanager=Custom('systemstatemanager(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxpi', graph_attr=nodeattr):
          led=Custom('led','./qakicons/symActorSmall.png')
          sonar=Custom('sonar','./qakicons/symActorSmall.png')
          sonarsimulator=Custom('sonarsimulator(coded)','./qakicons/codedQActor.png')
          sonardatasource=Custom('sonardatasource(coded)','./qakicons/codedQActor.png')
     led >> Edge(color='blue', style='solid', xlabel='updateled', fontcolor='blue') >> systemstatemanager
     sonar >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonarsimulator
     sonar >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonardatasource
diag
