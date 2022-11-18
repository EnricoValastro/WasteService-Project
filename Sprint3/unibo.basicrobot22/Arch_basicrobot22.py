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
with Diagram('basicrobot22Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
          envsonarhandler=Custom('envsonarhandler','./qakicons/symActorSmall.png')
          pathexec=Custom('pathexec','./qakicons/symActorSmall.png')
          pathobs=Custom('pathobs','./qakicons/symActorSmall.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     sys >> Edge(color='red', style='dashed', xlabel='sonar', fontcolor='red') >> envsonarhandler
     pathexec >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='alarm', fontcolor='red') >> pathexec
     pathexec >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     pathexec >> Edge(color='blue', style='solid', xlabel='coapUpdate', fontcolor='blue') >> pathobs
diag
