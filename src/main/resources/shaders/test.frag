#version 410 core

#extension GL_ARB_gpu_shader_fp64 : enable

out vec4 FragColor;
in vec3 outColor;
uniform int width, height;
uniform double scale;
uniform double offsetX, offsetY;
double aspectRatio = double( width ) / double( height );
double size = scale;
double localWidthMin = -size * aspectRatio + offsetX, localWidthMax = size * aspectRatio + offsetX;
double localHeightMin = -size + offsetY, localHeightMax = size + offsetY;

float maxIter = 150.0;
float colorScale = maxIter / 255.0;

double remap( double x, double x1, double x2, double y1, double y2 ) {
    return y1 + ( ( x - x1 ) * ( y2 -y1 ) ) / ( x2 - x1 );
}

dvec2 toLocal( vec2 global ) {
    return dvec2(
            remap( global.x, 0, width, localWidthMin, localWidthMax ),
            remap( global.y, 0, height, localHeightMin, localHeightMax )
    );
}

void main() {
    dvec2 pixels = toLocal( gl_FragCoord.xy );
    double x = pixels.x;
    double y = pixels.y;

    float zx = 0.0f, zy = 0.0f;
    float x2, y2;

    int i = 0;

    while ( i < maxIter ) {
        x2 = zx * zx;
        y2 = zy * zy;

        if ( x2 + y2 > 4.0 ) break;

        float yNew = fma( 2.0f * zx, zy, float( y ) );
        zx = fma( -zy, zy, x2 + float( x ) );
        zy = yNew;
        i++;
    }
    float color = 0;
    if ( i < maxIter ) color = float( remap( i / colorScale, 0, 255, 0, 1 ) );

    FragColor = vec4( color, 0, color, 1.0f );
}

