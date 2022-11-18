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
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          pathexec=Custom('pathexec(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxpi', graph_attr=nodeattr):
          led=Custom('led(ext)','./qakicons/externalQActor.png')
          sonarqak22varesi=Custom('sonarqak22varesi(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxwasteservice', graph_attr=nodeattr):
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolleycore=Custom('transporttrolleycore','./qakicons/symActorSmall.png')
          transporttrolleymover=Custom('transporttrolleymover','./qakicons/symActorSmall.png')
          transporttrolleyexecutor=Custom('transporttrolleyexecutor','./qakicons/symActorSmall.png')
          systemstatemanager=Custom('systemstatemanager','./qakicons/symActorSmall.png')
          sonarfilter=Custom('sonarfilter','./qakicons/symActorSmall.png')
     sys >> Edge(color='red', style='dashed', xlabel='local_dropoutdone', fontcolor='red') >> wasteservice
     wasteservice >> Edge(color='blue', style='solid', xlabel='updatecontainer', fontcolor='blue') >> systemstatemanager
     wasteservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolleycore
     wasteservice >> Edge(color='blue', style='solid', xlabel='dropout', fontcolor='blue') >> transporttrolleycore
     wasteservice >> Edge(color='blue', style='solid', xlabel='gotohome', fontcolor='blue') >> transporttrolleycore
     wasteservice >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> systemstatemanager
     transporttrolleycore >> Edge(color='magenta', style='solid', xlabel='moveto', fontcolor='magenta') >> transporttrolleymover
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='blink', fontcolor='blue') >> led
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='updateled', fontcolor='blue') >> systemstatemanager
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='updatetrolley', fontcolor='blue') >> systemstatemanager
     sys >> Edge(color='red', style='dashed', xlabel='alarm', fontcolor='red') >> transporttrolleycore
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='turnon', fontcolor='blue') >> led
     sys >> Edge(color='red', style='dashed', xlabel='local_resume', fontcolor='red') >> transporttrolleycore
     transporttrolleycore >> Edge(color='magenta', style='solid', xlabel='execaction', fontcolor='magenta') >> transporttrolleyexecutor
     transporttrolleycore >> Edge( xlabel='local_dropoutdone', **eventedgeattr, fontcolor='red') >> sys
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='turnoff', fontcolor='blue') >> led
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> wasteservice
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> transporttrolleycore
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> transporttrolleyexecutor
     transporttrolleycore >> Edge(color='blue', style='solid', xlabel='exit', fontcolor='blue') >> transporttrolleymover
     transporttrolleymover >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexec
     sys >> Edge(color='red', style='dashed', xlabel='local_resume', fontcolor='red') >> transporttrolleymover
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> sonarfilter
     sonarfilter >> Edge( xlabel='alarm', **eventedgeattr, fontcolor='red') >> sys
     sonarfilter >> Edge( xlabel='local_resume', **eventedgeattr, fontcolor='red') >> sys
diag
