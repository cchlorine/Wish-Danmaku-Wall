const fs = require('fs')
const path = require('path')
const Fontmin = require('fontmin')

const words = fs.readFileSync(path.resolve(__dirname, '3500words.txt'), 'utf-8')

const assetsFonts = path.resolve(__dirname, '../src/assets/fonts/*.TTF')
const minset = new Fontmin()
    .src(assetsFonts)
    .use(Fontmin.glyph({
        text: words,
        hinting: true
    }))
    .use(Fontmin.ttf2eot())
    .use(Fontmin.ttf2woff({
        deflate: true           // deflate woff. default = false
    }))
    .use(Fontmin.ttf2woff2())
    .use(Fontmin.ttf2svg())
    // .use(svgo())
    .use(Fontmin.css({
        fontPath: './',         // location of font file
        base64: false,           // inject base64 data:application/x-font-ttf; (gzip font with css).
                                // default = false
        glyph: false,            // generate class for each glyph. default = false
        fontFamily: 'sixLineSmall',
        asFileName: true,      // rewrite fontFamily as filename force. default = false
        local: true             // boolean to add local font. default = false
    }))
    .dest(path.resolve(__dirname, '../public/fonts/small'))

const fullset = new Fontmin()
    .src(assetsFonts)
    .dest(path.resolve(__dirname, '../public/fonts/full'))

minset.run(function (err, files) {
    if (err) {
        throw err;
    }

    console.log(files);
    // => { contents: <Buffer 00 01 00 ...> }
});
