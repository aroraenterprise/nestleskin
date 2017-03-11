from flask import Flask, request
from flask_restplus import Resource, Api
from werkzeug.datastructures import FileStorage
from PIL import Image
from scipy.cluster.vq import kmeans, whiten
import io
import numpy

app = Flask(__name__)
api = Api(app)

upload_parser = api.parser()
upload_parser.add_argument('file', location='files', type=FileStorage, required=True)

def getPixels(bytes):
	picture = Image.open(bytes)
	pixels = numpy.array(picture.getdata()).reshape(picture.size[0], picture.size[1], -1).astype(float)
	return pixels

def getCentroids(pixels):
	observations = numpy.reshape(pixels, (-1, 3))
	whitened = whiten(observations)
	centroids = kmeans(observations, 2)
	return centroids

@api.route('/upload/')
@api.expect(upload_parser)
class Upload(Resource):
	def post(self):
		pixels = getPixels(bytes=request.files['file'])
		centroids = getCentroids(pixels=pixels)
		print centroids
		return "good shit"

if __name__ == '__main__':
	app.run(host='0.0.0.0', debug=True)