#version 410 core

#extension GL_ARB_gpu_shader_fp64 : enable

out vec4 FragColor;
in vec3 outColor;

uniform int width, height;
uniform float scale;
uniform float offsetX, offsetY;
uniform float time;

float aspectRatio = float( width ) / float( height );
float size = scale;
float localWidthMin = -size * aspectRatio + offsetX, localWidthMax = size * aspectRatio + offsetX;
float localHeightMin = -size + offsetY, localHeightMax = size + offsetY;

int maxIter = int( 2000 * abs( tan( time ) ) );
float colorScale = float( maxIter );

float remap( float x, float x1, float x2, float y1, float y2 ) {
    return y1 + ( ( x - x1 ) * ( y2 -y1 ) ) / ( x2 - x1 );
}

vec2 toLocal( vec2 global ) {
    return vec2(
            remap( global.x, 0, width, float( localWidthMin ), float( localWidthMax ) ),
            remap( global.y, 0, height, float( localHeightMin ), float( localHeightMax ) )
    );
}

void main() {
    vec2 pixels = toLocal( gl_FragCoord.xy );
    float x = pixels.x;
    float y = pixels.y;

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
    float color = 1.0;
    if ( i < maxIter ) color = float( i ) / colorScale;

    FragColor = vec4( color * 2, color / 2, color, 1.0f );
}

