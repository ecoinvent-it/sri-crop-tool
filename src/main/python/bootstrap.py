from http.server import BaseHTTPRequestHandler, HTTPServer
import json
from modelsSequence import ModelsSequence

#TODO: Read from external properties
hostName = "localhost"
hostPort = 11001

class QtsRequestHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        #TODO: Add a context URL?
        if (self.path == "/computeLci"):
            try:
                mapReq = json.loads(str(self.rfile.read(int(self.headers['content-length'])), "utf-8"))
                #TODO: Do the things
                mapRes = ModelsSequence(mapReq).executeSequence()
                jsonRes = json.dumps(mapRes)
            except:
                self.send_error(500)
                raise
            self.send_response(200)
            self.send_header("Content-type", "application/json")
            self.end_headers()
            self.wfile.write(bytes(jsonRes, "utf-8"))
        else:
            self.send_error(404)
                         
if __name__ == '__main__':
    webServer = HTTPServer((hostName, hostPort), QtsRequestHandler)
    webServer.serve_forever()