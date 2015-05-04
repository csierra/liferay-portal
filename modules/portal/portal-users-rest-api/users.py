from httplib import HTTPConnection
from base64 import b64encode
from json.tool import json as jsontool
from PIL import Image
from StringIO import StringIO
from xml.etree import ElementTree as ET

headers = {'Accept-Language': 'en'}
es = 'es'
en = 'en'
de = 'de'

currentPage = 1
per_page = 300

parse = lambda f: eval(f())

def parse(f):
    try:
        return parser(f)
    except: 
        return None
    
def conn():
    connection = HTTPConnection("localhost", 8080)
    connection.set_debuglevel(1)
    return connection

def json(): 
    global headers, parser

    headers["Accept"] = "application/json"
    headers["Content-Type"] = "application/json"
    parser = lambda f: jsontool.loads(f()) 

def xml(): 
    global headers, parser

    headers["Accept"] = "application/xml"
    headers["Content-Type"] = "application/xml"
    parser = lambda x: x()
    
   
def auth(user="test@liferay.com", password="test"):
    headers["Authorization"] = "Basic %s" % b64encode(b"%s:%s" % (user, password)).decode("iso-8859-1")

def noauth():
    del headers["Authorization"]

def use(*args):
    for f in args:
        res = f()
    return res

def processLinks(links, target):
    if links:
        for link in links.split(','):
            et = ET.fromstring(link)
            rel = et.attrib['rel']
            href = et.attrib['href']
            target[rel + "page"] = lambda href=href: get(href)

def get(url):
    global headers
    c = conn()
    c.request("GET", url, "", headers)
    res = c.getresponse()
    responseheaders = res.getheaders()
    processLinks(res.getheader('link'), globals())
    print res.status, res.reason
    if res.status == 200:
        return res.read()
    else: return ""

def perpage(n):
    global per_page
    per_page = n

def page(n):
    global currentPage
    currentPage = n

def all():
    global currentPage, per_page
    return get("/o/rest/api/users?page=" + str(currentPage) + "&per_page=" + str(per_page))

def user(userId):
    return lambda: get("/o/rest/api/users/" + str(userId))

def prefixes():
    return get("/o/rest/api/users/prefixes")

def lang(l):
    global headers
    headers['Accept-Language'] = l

def processLink(link):
    return link.replace("http://localhost:8080", "")

def portrait(userId):
    u = parse(user(userId))
    return get(processLink(u['portraitLink']['url']))

def show(b):
    Image.open(StringIO(b)).show()

def createUser():
    data = collect(['firstName', 'lastName', 'emailAddress', 'screenName'])
    c = conn()
    c.request("POST", "/o/rest/api/users", jsontool.dumps(data), headers)
    res = c.getresponse()
    print res.status, res.reason
    if res.status == 201:
        return res.read()
    else: return ""

def delete(userId):
    c = conn()
    c.request("DELETE", "/o/rest/api/users/" + str(userId), "", headers)
    res = c.getresponse()
    print res.status, res.reason

def collect(data):
    d = {}
    for k in data:
        d[k] = raw_input(k + ": ")

    return d

use(json, auth)
