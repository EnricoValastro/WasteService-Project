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
with Diagram('transporttrolleyArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxtransporttrolley', graph_attr=nodeattr):
          transporttrolleycore=Custom('transporttrolleycore','./qakicons/symActorSmall.png')
          transporttrolleymover=Custom('transporttrolleymover','./qakicons/symActorSmall.png')
          transporttrolleyexecutor=Custom('transporttrolleyexecutor','./qakicons/symActorSmall.png')
     transporttrolleycore >> Edge(color='magenta', style='solid', xlabel='moveto', fontcolor='magenta') >> transporttrolleymover
     transporttrolleycore >> Edge(color='magenta', style='solid', xlabel='execaction', fontcolor='magenta') >> transporttrolleyexecutor
diag