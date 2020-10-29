uniform mat4 modelViewProjection;
attribute vec4 position;
attribute vec2 texCoordinate;

varying vec2 vTexCoordinate;

void main() {
    vTexCoordinate = texCoordinate;
    gl_Position = modelViewProjection * position;
    gl_PointSize = 8.0;
}