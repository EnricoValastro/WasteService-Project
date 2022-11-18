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
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepdone') >> sys 
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepfail') >> sys 
     sys >> Edge(color='red', style='dashed', xlabel='sonar') >> envsonarhandler
     pathexec >> Edge(color='blue', style='solid', xlabel='cmd') >> basicrobot
     pathexec >> Edge(color='magenta', style='solid', xlabel='step') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='alarm') >> pathexec
     pathexec >> Edge(color='green', style='dashed', xlabel='dopathdone') >> sys 
     pathexec >> Edge(color='green', style='dashed', xlabel='dopathfail') >> sys 
     pathexec >> Edge(color='blue', style='solid', xlabel='coapUpdate') >> pathobs
diag
