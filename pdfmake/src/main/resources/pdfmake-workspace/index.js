const fonts = {
  'Malgun Gothic': {
    normal: 'fonts/MalgunGothic.ttf',
    bold: 'fonts/MalgunGothic.ttf',
    italics: 'fonts/MalgunGothic.ttf',
    bolditalics: 'fonts/MalgunGothic.ttf'
  }
};

const PdfPrinter = require('pdfmake');
const fs = require('fs');
const printer = new PdfPrinter(fonts);

function make(input, output) {
  return new Promise((resolve, reject) => {
    const text = fs.readFileSync(input, 'utf8');
    const definition = new Function("return " + text).call()
    const pdfDoc = printer.createPdfKitDocument(definition);
    const writer = fs.createWriteStream(output);
    writer.on('finish', function () {
      resolve()
    });
    pdfDoc.pipe(writer);
    pdfDoc.end();

  })
}

const args = process.argv.slice(2)
const input = args[0]
const output = args[1]

make(input, output).then(() => {
  process.exit(1)
})

