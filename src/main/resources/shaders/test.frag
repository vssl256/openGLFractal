#version 400

out vec4 FragColor;
in vec3 outColor;
uniform int width, height;
uniform float scale;
uniform float offsetX, offsetY;
float aspectRatio = float( width ) / float( height );
float size = 10 * scale;
float localWidthMin = -size * aspectRatio + offsetX, localWidthMax = size * aspectRatio + offsetX;
float localHeightMin = -size + offsetY, localHeightMax = size + offsetY;
float oneOverThree = 1.0 / 3.0;

float pi = 3.14159;

float remap( float x, float x1, float x2, float y1, float y2 ) {
    return y1 + ( ( x - x1 ) * ( y2 -y1 ) ) / ( x2 - x1 );
}

vec2 toLocal( vec2 global ) {
    return vec2(
            remap( global.x, 0, width, localWidthMin, localWidthMax ),
            remap( global.y, 0, height, localHeightMin, localHeightMax )
    );
}

void main() {
    vec2 pixels = toLocal( gl_FragCoord.xy );
    float x = pixels.x;
    float y = pixels.y;
    float axisThreshold = 0.01;
    float threshold = 0.1;


    float graph1 = abs( sin( x ) - y  );
    float graph2 = abs( cos( x ) - y ) ;

    vec3 color = vec3( 0.0, 0.5, 0.5 );
    if ( abs(x) <= axisThreshold * scale * 2.5 || abs(y) <= axisThreshold * scale * 2.5 ) {
        color.rgb = vec3( 1.0, 1.0, 1.0 );
    }

    if ( graph1 <= threshold + scale * 0.15 ) {
        color.rgb = vec3( 1.0, 1.0, 1.0 );
    }
    if ( graph1 <= threshold + scale * 0.1 ) {
        color.rgb = vec3( 0.0, 0.0, 1.0 );
    }

    if ( graph2 <= threshold + scale * 0.15 ) {
        color.rgb = vec3( 1.0, 1.0, 1.0 );
    }
    if ( graph2 <= threshold + scale * 0.1 ) {
        color.rgb = vec3( 0.4, 1.0, 0.0 );
    }
    FragColor = vec4( color.r, color.g, color.b, 1.0f );
}

