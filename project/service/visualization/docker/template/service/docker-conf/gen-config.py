import os

conf = open("k8s-application.yml", "r").read()
conf = conf.replace("%{GATEWAY_HOST}", os.environ["GATEWAY_HOST"])
conf = conf.replace("%{GATEWAY_PORT}", os.environ["GATEWAY_PORT"])

fw = open("application.yml", "w")
fw.write(conf)
