#version 400

out vec4 FragColor;
in vec3 outColor;
uniform int width, height;
uniform float time;
uniform float scale;
uniform float offsetX, offsetY;
float aspectRatio = float( width ) / float( height );
float size = 10 * scale;
float localWidthMin = -size * aspectRatio + offsetX, localWidthMax = size * aspectRatio + offsetX;
float localHeightMin = -size + offsetY, localHeightMax = size + offsetY;

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
    float threshold = 0.01;
    float color = 0.0f;
    float graph = abs( cos(time * x) * x - y );
    if ( graph <= threshold ) {
        color = 0.8f;
    }
    FragColor = vec4( color * 0.6, color, 0, 1.0f );
}

