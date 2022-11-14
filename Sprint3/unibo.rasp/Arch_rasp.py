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
with Diagram('raspArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxpi', graph_attr=nodeattr):
          led=Custom('led','./qakicons/symActorSmall.png')
          sonarqak22varesi=Custom('sonarqak22varesi','./qakicons/symActorSmall.png')
          sonardatasource=Custom('sonardatasource(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
     sonarqak22varesi >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonarqak22varesi
     sonarqak22varesi >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonardatasource
     sys >> Edge(color='red', style='dashed', xlabel='sonar', fontcolor='red') >> sonarqak22varesi
     sonarqak22varesi >> Edge(color='blue', style='solid', xlabel='sonardeactivate', fontcolor='blue') >> sonardatasource
     sonarqak22varesi >> Edge( xlabel='sonardata', **eventedgeattr, fontcolor='red') >> sys
diag
