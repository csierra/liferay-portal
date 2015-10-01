from suds.client import Client
from suds.transport.http import HttpAuthenticated
from suds.cache import NoCache

t = HttpAuthenticated(username='test@liferay.com', password='test')

client = Client('http://localhost:8080/o/soap/UsersSoap?wsdl', transport=t, cache=NoCache())

list = client.service.list

getUser = client.service.getUser