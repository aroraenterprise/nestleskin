from flask import Flask, request
from flask_restplus import Resource, Api
from werkzeug.datastructures import FileStorage
from PIL import Image
import io
import numpy

app = Flask(__name__)
api = Api(app)

upload_parser = api.parser()
upload_parser.add_argument('file', location='files', type=FileStorage, required=True)

def getPixels(bytes):
	picture = Image.open(bytes)
	pixels = numpy.array(picture.getdata()).reshape(picture.size[0], picture.size[1], 4)
	return pixels

@api.route('/upload/')
@api.expect(upload_parser)
class Upload(Resource):
	def post(self):
		pixels = getPixels(bytes=request.files['file'])
		return 'good shit'

if __name__ == '__main__':
	app.run(host='0.0.0.0', debug=True)